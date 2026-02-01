package com.duri.duricore.chatting.service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.duri.duricore.chatting.entity.cassandra.ChatMessageByRoom;
import com.duri.duricore.chatting.entity.cassandra.ChatMessageByRoomKey;
import com.duri.duricore.chatting.entity.cassandra.ChatMessageBySender;
import com.duri.duricore.chatting.entity.cassandra.ChatMessageBySenderKey;
import com.duri.duricore.chatting.repository.cassandra.ChatMessageByRoomRepository;
import com.duri.duricore.chatting.repository.cassandra.ChatMessageBySenderRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

    private final ChatMessageByRoomRepository chatMessageByRoomRepository;
    private final ChatMessageBySenderRepository chatMessageBySenderRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "chat:room:";
    private static final int MAX_RECENT_MESSAGES = 200;
    private static final Duration REDIS_TTL = Duration.ofHours(24);

    /**
     *  메시지 저장
     *
     * @return 메시지 저장된 시각
     */
    public LocalDateTime saveMessage(ChatMessageByRoom chatMessage) {
        String redisListKey = REDIS_KEY_PREFIX + chatMessage.getKey().getRoomId();
        String redisSetKey = redisListKey + ":ids";

        // 영속성 확보를 위한 Cassandra 우선 저장
        saveToCassandra(chatMessage);

        // Redis 중복 확인 후 push
        Long added = redisTemplate.opsForSet().add(redisSetKey, chatMessage.getMessageId());
        if (added != null && added > 0L) {
            redisTemplate.opsForList().rightPush(redisListKey, chatMessage);
            // 최신 N개만 유지
            redisTemplate.opsForList().trim(redisListKey, -MAX_RECENT_MESSAGES, -1);
            redisTemplate.expire(redisListKey, REDIS_TTL);
            redisTemplate.expire(redisSetKey, REDIS_TTL);
        }

        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(
                        Uuids.unixTimestamp(
                                chatMessage.getKey().getCreatedAt()
                        )
                ),
                ZoneId.systemDefault());
    }

    /**
     * Cassandra 저장(Room / Sender)
     */
    private void saveToCassandra(ChatMessageByRoom chatMessage) {
        // room
        chatMessageByRoomRepository.save(chatMessage);

        // sender
        chatMessageBySenderRepository.save(new ChatMessageBySender(
                chatMessage.getSenderId(),
                chatMessage.getKey().getRoomId(),
                chatMessage.getMessageId(),
                chatMessage.getContent(),
                chatMessage.getMessageType()
        ));
    }

    /**
      최근 메시지 조회
     */
    public List<ChatMessageByRoom> getRecentMessages(Long roomId, int size) {
        String redisKey = REDIS_KEY_PREFIX + roomId;
        List<Object> cached = redisTemplate.opsForList().range(redisKey, -size, -1);

        //캐시 반환
        if (cached != null && !cached.isEmpty()) {
            return cached.stream()
                    .map(o -> (ChatMessageByRoom) o)
                    .toList();
        }

        // Redis에 없을 시 Cassandra에서 조회 후 캐시에 push
        Slice<ChatMessageByRoom> slice = chatMessageByRoomRepository.findByKeyRoomId(
                roomId,
                PageRequest.of(0,size)
        );
        List<ChatMessageByRoom> messages = slice.getContent();

        messages.forEach(msg -> {
            redisTemplate.opsForList().rightPush(redisKey, msg);
            redisTemplate.opsForSet().add(redisKey + ":ids", msg.getMessageId());
        });
        redisTemplate.opsForList().trim(redisKey, -MAX_RECENT_MESSAGES, -1);
        redisTemplate.expire(redisKey, REDIS_TTL);
        redisTemplate.expire(redisKey + ":ids", REDIS_TTL);

        return messages;
    }

    /**
     * 특정 시점 이전 메시지 조회
     *
     * @param roomId
     * @param beforeCreatedAt
     * @param size
     * @return
     */
    public Slice<ChatMessageByRoom> getOlderMessages(Long roomId, UUID beforeCreatedAt, int size) {
        return chatMessageByRoomRepository.findOlderThan(
                roomId,
                beforeCreatedAt,
                PageRequest.ofSize(size)
        );
    }

    /**
     * sender 기준 메시지 조회
     */
    public List<ChatMessageBySender> getMessagesBySender(Long senderId, int size) {
        return chatMessageBySenderRepository.findBykeySenderId(
                senderId, PageRequest.of(0, size)).
                getContent();
    }
}

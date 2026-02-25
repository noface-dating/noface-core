package com.duri.duricore.chat.controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.duri.duricore.chat.dto.rest.*;
import com.duri.duricore.chat.entity.cassandra.ChatMessageByRoom;
import com.duri.duricore.chat.service.cassandra.ChatMessageService;
import com.duri.duricore.chat.service.mysql.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 채팅에 관련된 HTTP작업을 수행하는 Controller Layer입니다.
 *
 * @author kyw10987
 */
@RestController
@RequestMapping("api/v1/chat-room")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    /**
     * 채팅방에 HTTP방식으로 채팅을 전송합니다. (구현이 다 되어있지 않습니다.)
     * 추후 필요시 수정 예정.
     *
     * @param chatMessageRequestDTO 채팅전송DTO
     * @return 전송성공시 (200 OK)
     */
    @PostMapping("/send")
    public ResponseEntity<SendChatMessageResponseDTO> sendMessage(@RequestBody ChatMessageRequestDTO chatMessageRequestDTO) {
        LocalDateTime timestamp = chatMessageService.saveMessage(new ChatMessageByRoom(
                chatMessageRequestDTO.roomId(),
                chatMessageRequestDTO.senderId(),
                chatMessageRequestDTO.content(),
                Enum.valueOf(
                        com.duri.duricore.chat.entity.cassandra.MessageType.class,
                        chatMessageRequestDTO.messageType()
                )
        ));

        return ResponseEntity.status(HttpStatus.OK).body(new SendChatMessageResponseDTO(timestamp));
    }

    /**
     * 최근 메시지 조회
     *
     * @param roomId roomId uniqueID
     * @param size 한번에 가져올 채팅(값이 없으면 50)
     * @return 채팅방 입장시 바로 표시되는 가장 최근 채팅 내역
     */
    @GetMapping("/recent")
    public List<ChatMessageResponseDTO> getRecentMessages(
            @RequestParam UUID roomId,
            @RequestParam(defaultValue = "50") int size
            ) {
        List<ChatMessageByRoom> messages = chatMessageService.getRecentMessages(
                roomId,
                size
        );
        Map<Long, String> users = chatRoomService.chatRoomUsersList(roomId);

        return messages.stream()
                .map(msg -> new ChatMessageResponseDTO(
                        msg.getKey().getRoomId(),
                        msg.getSenderId(),
                        users.get(msg.getSenderId()),
                        msg.getContent(),
                        msg.getMessageType().name(),
                        msg.getMessageId(),
                        msg.getKey().getCreatedAt(),
                        LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(
                                        Uuids.unixTimestamp(
                                                msg.getKey().getCreatedAt()
                                        )
                                ),
                                ZoneId.systemDefault())
                ))
                .collect(Collectors.toList());
    }

    /**
     * 특정 시점 이전 메시지 조회
     *
     * @param roomId roomId uniqueID
     * @param beforeCreatedAt 기준 시점
     * @param size 한번에 가져올 채팅(값이 없으면 50)
     * @return 채팅 내역
     */
    @GetMapping("/older")
    public ResponseEntity<ChatMessageSliceResponseDTO> getOlderMessages(
            @RequestParam UUID roomId,
            @RequestParam UUID beforeCreatedAt,
            @RequestParam(defaultValue = "50") int size
    ) {
        Slice<ChatMessageByRoom> slice = chatMessageService.getOlderMessages(roomId, beforeCreatedAt, size);
        Map<Long, String> users = chatRoomService.chatRoomUsersList(roomId);

        List<ChatMessageResponseDTO> messages = slice.getContent()
                .stream()
                .map(msg -> new ChatMessageResponseDTO(
                        msg.getKey().getRoomId(),
                        msg.getSenderId(),
                        users.get(msg.getSenderId()),
                        msg.getContent(),
                        msg.getMessageType().name(),
                        msg.getMessageId(),
                        msg.getKey().getCreatedAt(),
                        LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(
                                        Uuids.unixTimestamp(
                                                msg.getKey().getCreatedAt()
                                        )
                                ),
                                ZoneId.systemDefault())
                ))
                .collect((Collectors.toList()));
        return ResponseEntity.status(HttpStatus.OK).body(new ChatMessageSliceResponseDTO(messages, slice.hasNext()));
    }
}

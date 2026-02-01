package com.duri.duricore.chatting.controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.duri.duricore.chatting.dto.*;
import com.duri.duricore.chatting.entity.cassandra.ChatMessageByRoom;
import com.duri.duricore.chatting.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/chat")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // 완료, 테스트 필요
    @PostMapping("/send")
    public ResponseEntity<SendChatMessageResponseDTO> sendMessage(@RequestBody ChatMessageRequestDTO chatMessageRequestDTO) {
        LocalDateTime timestamp = chatMessageService.saveMessage(new ChatMessageByRoom(
                chatMessageRequestDTO.roomId(),
                chatMessageRequestDTO.messageId(),
                chatMessageRequestDTO.senderId(),
                chatMessageRequestDTO.content(),
                Enum.valueOf(
                        com.duri.duricore.chatting.entity.cassandra.MessageType.class,
                        chatMessageRequestDTO.messageType()
                )
        ));

        return ResponseEntity.status(HttpStatus.OK).body(new SendChatMessageResponseDTO(timestamp));
    }

    //TODO 로직 수정
    /**
     * 최근 메시지 조회
     */
    @PostMapping("/recent")
    public List<ChatMessageResponseDTO> getRecentMessages(
            @RequestParam(defaultValue = "50") int size,
            @RequestBody RecentChatMessageRequestDTO recentChatMessageRequestDTO
            ) {
        List<ChatMessageByRoom> messages = chatMessageService.getRecentMessages(
                recentChatMessageRequestDTO.roomId(),
                size
        );
        return messages.stream()
                .map(msg -> new ChatMessageResponseDTO(
                        msg.getKey().getRoomId(),
                        msg.getSenderId(),
                        msg.getContent(),
                        msg.getMessageType().name(),
                        msg.getMessageId(),
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

    // TODO 로직 수정
    /**
     * 특정 시점 이전 메시지 조회
     */
    @GetMapping("/older")
    public ResponseEntity<ChatMessageSliceResponseDTO> getOlderMessages(
            @RequestParam Long roomId,
            @RequestParam UUID beforeCreatedAt,
            @RequestParam(defaultValue = "50") int size
    ) {
        Slice<ChatMessageByRoom> slice = chatMessageService.getOlderMessages(roomId, beforeCreatedAt, size);

        List<ChatMessageResponseDTO> messages = slice.getContent()
                .stream()
                .map(msg -> new ChatMessageResponseDTO(
                        msg.getKey().getRoomId(),
                        msg.getSenderId(),
                        msg.getContent(),
                        msg.getMessageType().name(),
                        msg.getMessageId(),
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

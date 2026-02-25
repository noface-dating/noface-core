package com.duri.duricore.chat.controller;

import com.duri.duricore.chat.dto.websocket.ChatMessageSocketRequestDTO;
import com.duri.duricore.chat.dto.websocket.ChatMessageSocketResponseDTO;
import com.duri.duricore.chat.entity.cassandra.ChatMessageByRoom;
import com.duri.duricore.chat.service.cassandra.ChatMessageService;
import com.duri.duricore.global.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * 채팅전송에 쓰이는 WebSocket & RabbitMQ 기반 Controller Layer 입니다.
 *
 * @author kyw10987
 */
@Controller
@RequiredArgsConstructor
public class ChatMessageWebSocketController {

    private final RabbitTemplate rabbitTemplate;
    private final ChatMessageService chatMessageService;

    /**
     * 메시지를 WebSocket으로 받는 메소드
     *
     * @param chatMessageSocketRequestDTO 메시지전송DTO
     */
    @MessageMapping("/chat.send")
    public void send(ChatMessageSocketRequestDTO chatMessageSocketRequestDTO) {
        ChatMessageByRoom chatMessageByRoom = new ChatMessageByRoom(
                chatMessageSocketRequestDTO.roomId(),
                chatMessageSocketRequestDTO.senderId(),
                chatMessageSocketRequestDTO.content(),
                Enum.valueOf(
                        com.duri.duricore.chat.entity.cassandra.MessageType.class,
                        chatMessageSocketRequestDTO.messageType()
                )
        );

        LocalDateTime createdAt = chatMessageService.saveMessage(chatMessageByRoom);

        ChatMessageSocketResponseDTO response = new ChatMessageSocketResponseDTO(
                        chatMessageSocketRequestDTO.roomId(),
                        chatMessageSocketRequestDTO.senderId(),
                        chatMessageSocketRequestDTO.content(),
                        chatMessageSocketRequestDTO.messageType(),
                        createdAt
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CHAT_EXCHANGE,
                "chat.room." + response.roomId(),
                response
        );
    }

    //TODO 나머지 코드 작성
}

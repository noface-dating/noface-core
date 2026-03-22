package com.duri.duricore.chat.consumer;

import com.duri.duricore.chat.dto.websocket.ChatMessageSocketResponseDTO;
import com.duri.duricore.global.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Rabbit MQ Listener 입니다. (채팅 전용)
 * 서버 이중화 시, 사용하는 메소드입니다.
 *
 * @author kyw10987
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ChatMessageConsumer {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "chat.server.${spring.application.name}", durable = "true"),
                    exchange = @Exchange(value = RabbitMQConfig.CHAT_EXCHANGE, type = "topic"),
                    key = "chat.room.*"
            )
    )
    public void consume(ChatMessageSocketResponseDTO chatMessageSocketResponseDTO) {

        log.info("chat.server.consume : {}", chatMessageSocketResponseDTO);

        simpMessagingTemplate.convertAndSend(
                "/sub/chat.room." + chatMessageSocketResponseDTO.roomId(),
                chatMessageSocketResponseDTO);
    }
}

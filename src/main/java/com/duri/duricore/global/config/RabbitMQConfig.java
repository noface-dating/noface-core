package com.duri.duricore.global.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CHAT_EXCHANGE = "chat.exchange";

    @Bean
    TopicExchange chatExchange() {
        return new TopicExchange(CHAT_EXCHANGE);
    }
}

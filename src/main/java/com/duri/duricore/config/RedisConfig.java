/**
 *
 * 일단 채팅에서만 redis 사용으로 알고 있고 맞춰진 config이 있어서 제거 하였습니다.
 *
 */

//package com.duri.duricore.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.RedisSerializer;
//
//@Configuration
//public class RedisConfig {
//
//	@Bean
//	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//		RedisTemplate<String, Object> template = new RedisTemplate<>();
//		template.setConnectionFactory(connectionFactory);
//
//		// Key 직렬화
//		template.setKeySerializer(RedisSerializer.string());
//		template.setHashKeySerializer(RedisSerializer.string());
//
//		// Value 직렬화
//		var jsonSerializer = RedisSerializer.json();
//		template.setValueSerializer(jsonSerializer);
//		template.setHashValueSerializer(jsonSerializer);
//
//		template.afterPropertiesSet();
//		return template;
//	}
//}

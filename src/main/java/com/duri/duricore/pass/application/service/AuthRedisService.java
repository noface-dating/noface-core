package com.duri.duricore.pass.application.service;

import com.duri.duricore.pass.presentation.dto.RegistrationSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthRedisService {

	private final RedisTemplate<String, Object> redisTemplate;
	private static final String PREFIX = "REG_TOKEN:";

	/**
	 * 포트원 인증 고객 정보를 Redis에 저장하고 임시 가입 토큰을 발행합니다.
	 * @param customer 포트원 API에서 추출한 검증된 고객 정보
	 * @return 발급된 UUID 토큰
	 */
	public String saveAuthInfo(String token ,RegistrationSession customer) {
		// 1. 가입 세션을 위한 랜덤 토큰 생성
		String key = PREFIX + token;

		try {
			// 2. Redis에 VerifiedCustomer 객체 저장
			redisTemplate.opsForValue().set(key, customer, Duration.ofMinutes(10));

			log.info(">>>> [Redis 저장 성공] Key: {}", key);

			return token;
		} catch (Exception e) {
			log.error(">>>> [Redis 저장 실패] Key: {}, 사유: {}", key, e.getMessage());
			throw new RuntimeException("인증 데이터 저장 중 오류가 발생했습니다.");
		}
	}

	/**
	 * 최종 회원가입 시 토큰으로 Redis에서 고객 정보를 조회합니다.
	 */
	public RegistrationSession getAuthInfo(String token) {
		String key = PREFIX + token;
		try {
			return (RegistrationSession) redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			log.error(">>>> [Redis 조회 에러] Key: {}, 사유: {}", key, e.getMessage());
			return null;
		}
	}

	/**
	 * 가입 절차 완료 후 Redis 임시 데이터를 삭제합니다.
	 */
	public void deleteAuthInfo(String token) {
		String key = PREFIX + token;
		redisTemplate.delete(key);
		log.info(">>>> [Redis 삭제 완료] Key: {}", key);
	}
}
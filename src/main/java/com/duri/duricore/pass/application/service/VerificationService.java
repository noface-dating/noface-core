package com.duri.duricore.pass.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.duri.duricore.pass.infrasturucture.portone.PortOneClient;
import com.duri.duricore.pass.presentation.dto.PortOneVerificationResponse;
import com.duri.duricore.pass.presentation.dto.RegistrationSession;
import com.duri.duricore.pass.presentation.dto.VerificationResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VerificationService {

	private final PortOneClient portOneClient;
	private final AuthRedisService authRedisService;

	public VerificationResult processVerification(String identityVerificationId) {
		log.info("==== [본인인증 검증 시작] ====");
		log.info("요청된 identityVerificationId: {}", identityVerificationId);

		// 1. 포트원 API 호출 (전체 응답 객체 수신)
		log.info("[Step 1] PortOne API 호출 시도...");
		PortOneVerificationResponse response = portOneClient.fetchVerification(identityVerificationId);

		// [로그] 응답 객체 및 고객 정보 존재 여부 확인
		if (response == null || response.verifiedCustomer() == null) {
			log.error("[Error] PortOne 응답 또는 고객 정보(VerifiedCustomer)가 null입니다.");
			throw new RuntimeException("인증 정보를 가져오지 못했습니다.");
		}

		String token = UUID.randomUUID().toString();
		// 2. Redis 저장 및 토큰 발행
		log.info("[Step 2] Redis에 RegistrationSession 임시 저장 시도...");
		RegistrationSession session = new RegistrationSession(response.verifiedCustomer(), response.channel());
		authRedisService.saveAuthInfo(token, session);
		log.info("[Step 2 성공] 발행된 registrationToken: {}", token);

		// 3. 성인 여부 체크
		boolean isAdult = checkAdult(session.customer().birthDate());
		log.info("==== [본인인증 검증 종료] ====");

		return new VerificationResult(
			true,
			token	,
			isAdult
		);
	}

	private boolean checkAdult(String birthDate) {
		log.info("[성인 체크] 입력된 생년월일: {}", birthDate);
		try {
			int birthYear = Integer.parseInt(birthDate.substring(0, 4));
			int currentYear = java.time.LocalDate.now().getYear();
			boolean result = (currentYear - birthYear) >= 19;
			log.info("판별 결과: 만 나이 기준 성인여부: {}", result);
			return result;
		} catch (Exception e) {
			log.error("[Error] 생년월일 파싱 중 오류 발생 (입력값: {}): {}", birthDate, e.getMessage());
			return false;
		}
	}
}
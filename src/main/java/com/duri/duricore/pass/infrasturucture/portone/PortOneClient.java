package com.duri.duricore.pass.infrasturucture.portone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.duri.duricore.pass.presentation.dto.PortOneVerificationResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PortOneClient {
	private final RestClient restClient;

	@Value("${portone.api.secret}")
	private String apiSecret;

	public PortOneClient(RestClient.Builder builder) {
		this.restClient = builder.baseUrl("https://api.portone.io").build();
	}

	public PortOneVerificationResponse fetchVerification(String verificationId) {
		log.info(">>>> [PortOne API Request] URL: https://api.portone.io/identity-verifications/{}", verificationId);

		try {
			// 1. 포트원 원본 응답 구조(PortOneVerificationResponse)로 받기
			PortOneVerificationResponse response = restClient.get()
				.uri("/identity-verifications/{id}", verificationId)
				.header("Authorization", "PortOne " + apiSecret)
				.retrieve()
				.onStatus(HttpStatusCode::isError, (req, res) -> {
					log.error(">>>> [PortOne API Error] Status Code: {}, Status Text: {}", res.getStatusCode(), res.getStatusText());
					throw new RuntimeException("포트원 서버에서 에러 응답을 보냈습니다.");
				})
				.body(PortOneVerificationResponse.class);
			// 2. 응답 데이터 상세 로깅
			if (response == null) {
				log.error(">>>> [PortOne API Error] Response body is NULL");
				return null;
			}

			log.info(">>>> [PortOne API Response] Status: {}", response.status());

			return response;

		} catch (Exception e) {
			log.error(">>>> [PortOne API Exception] 호출 중 예외 발생: {}", e.getMessage());
			e.printStackTrace(); // 상세한 스택 트레이스 출력
			throw e;
		}
	}
}
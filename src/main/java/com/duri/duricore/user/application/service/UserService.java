package com.duri.duricore.user.application.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.duri.duricore.pass.application.service.AuthRedisService;
import com.duri.duricore.pass.presentation.dto.RegistrationSession;
import com.duri.duricore.pass.presentation.dto.VerificationChannel;
import com.duri.duricore.pass.presentation.dto.VerifiedCustomer;
import com.duri.duricore.user.domain.entity.User;
import com.duri.duricore.user.domain.repository.UserRepository;
import com.duri.duricore.user.exception.EmailAlreadyExistException;
import com.duri.duricore.user.exception.UsernameAlreadyExistException;
import com.duri.duricore.user.presentation.dto.SignUpRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final UserRepository userRepository;
	private final AuthRedisService authRedisService;
	private final PasswordEncoder passwordEncoder;

	public void signUp(SignUpRequest request){
		log.info("회원가입 시작");
		RegistrationSession session = authRedisService.getAuthInfo(request.registrationToken());
		if (session == null) {
			throw new RuntimeException("인증 시간이 만료되었습니다. 다시 인증해주세요.");
		}
		VerifiedCustomer customer = session.customer();
		VerificationChannel channel = session.channel();

		if (userRepository.existsByUsername(request.username())) {
			throw new UsernameAlreadyExistException("이미 사용 중인 아이디입니다.");
		}
		if (userRepository.existsByEmail(request.email())) {
			throw new EmailAlreadyExistException("이미 사용 중인 이메일입니다.");
		}

		User user = User.builder()
			.username(request.username())
			.password(passwordEncoder.encode(request.password()))
			.email(request.email())
			.platformType(channel.pgProvider())
			.lastLoginAt(LocalDateTime.now())
			.build();

		user.registerDI(customer.di());

		userRepository.save(user);

		authRedisService.deleteAuthInfo(request.registrationToken());

		log.info("회원가입 완료: {}", user.getUsername());
	}
}

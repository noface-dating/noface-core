package com.duri.duricore.pass.presentation.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duri.duricore.pass.application.service.VerificationService;
import com.duri.duricore.pass.presentation.dto.VerificationResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class VerificationController {
	private final VerificationService verificationService;

	@PostMapping("/verify")
	public ResponseEntity<VerificationResult> verify(@RequestBody Map<String, String> request) {
		String id = request.get("identityVerificationId");
		VerificationResult result = verificationService.processVerification(id);

		if (!result.success()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
		return ResponseEntity.ok(result);
	}
}

package com.duri.duricore.user.presentation.dto;

public record SignUpRequest(
	String username,
	String password,
	String email,
	String registrationToken
) {
}

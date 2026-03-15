package com.duri.duricore.pass.presentation.dto;

public record RegistrationSession(
	VerifiedCustomer customer,
	VerificationChannel channel
) {}
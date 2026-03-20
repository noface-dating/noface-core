package com.duri.duricore.pass.presentation.dto;

public record PortOneVerificationResponse(
	String id,
	String status,
	VerifiedCustomer verifiedCustomer,
	VerificationChannel channel
) {}
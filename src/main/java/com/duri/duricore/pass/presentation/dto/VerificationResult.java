package com.duri.duricore.pass.presentation.dto;

public record VerificationResult (
	boolean success,
	String registrationToken,
	boolean isAdult
) {}

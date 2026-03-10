package com.duri.duricore.pass.presentation.dto;

public record VerifiedCustomer(
	String name,
	String birthDate,
	String gender,
	String phoneNumber,
	String ci,
	String di
) {
	public VerifiedCustomer {
	if (di == null) {
		di = "TEST_DI_" + java.util.UUID.randomUUID().toString().substring(0, 8);
	}
}
}
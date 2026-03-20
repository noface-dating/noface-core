package com.duri.duricore.user.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_di")
@NoArgsConstructor
@AllArgsConstructor
class UserDI {
	@Id
	private String di;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
}
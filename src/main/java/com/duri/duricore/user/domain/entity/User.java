package com.duri.duricore.user.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(unique = true, nullable = false)
	private String username; // 로그인 ID

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String email;

	@Column(name = "platform_type")
	private String platformType;

	@Column(name = "last_login_at")
	private LocalDateTime lastLoginAt;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private UserDI userDI;

	@Builder
	public User(String username, String password, String email,String platformType, UserDI userDI, LocalDateTime lastLoginAt) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.platformType = platformType;
		this.userDI =userDI;
		this.lastLoginAt = lastLoginAt;
	}
	public void registerDI(String diValue) {
		this.userDI = new UserDI(diValue, this);
	}
}

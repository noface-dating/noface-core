package com.duri.duricore.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duri.duricore.user.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}

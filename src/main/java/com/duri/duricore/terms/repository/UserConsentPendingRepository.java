package com.duri.duricore.terms.repository;

import com.duri.duricore.terms.entity.UserConsentPending;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConsentPendingRepository extends JpaRepository<UserConsentPending, Long> {

    Optional<UserConsentPending> findByConsentToken(String consentToken);
}

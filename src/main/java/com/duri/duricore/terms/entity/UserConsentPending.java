package com.duri.duricore.terms.entity;

import com.duri.duricore.terms.entity.enums.ConsentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_consent_pending")
public class UserConsentPending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String consentToken;

    @Column(nullable = false)
    private Long termId;

    @Column(nullable = false, length = 32)
    private String agreedVersion;

    @Column(nullable = false)
    private boolean required;

    @Column(nullable = false)
    private LocalDateTime agreedAt;

    @Column(length = 64)
    private String ip;

    @Column(length = 255)
    private String userAgent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ConsentStatus status;

    private LocalDateTime expiresAt;
}

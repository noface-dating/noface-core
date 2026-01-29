package com.duri.duricore.terms.service;

import com.duri.duricore.terms.dto.ConsentRequest;
import com.duri.duricore.terms.dto.ConsentTokenResponse;
import com.duri.duricore.terms.dto.TermResponse;
import com.duri.duricore.terms.entity.Term;
import com.duri.duricore.terms.entity.UserConsentPending;
import com.duri.duricore.terms.entity.enums.ConsentStatus;
import com.duri.duricore.terms.exception.TermsErrorCode;
import com.duri.duricore.terms.exception.TermsException;
import com.duri.duricore.terms.repository.TermRepository;
import com.duri.duricore.terms.repository.UserConsentPendingRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TermsService {

    private static final long CONSENT_TOKEN_TTL_HOURS = 48;

    private final TermRepository termRepository;
    private final UserConsentPendingRepository userConsentPendingRepository;

    public TermsService(
            TermRepository termRepository, UserConsentPendingRepository userConsentPendingRepository) {
        this.termRepository = termRepository;
        this.userConsentPendingRepository = userConsentPendingRepository;
    }

    @Transactional(readOnly = true)
    public TermResponse getActiveTerms() {
        List<TermResponse.TermItem> items = termRepository.findAllByActiveTrue().stream()
                .map(term -> new TermResponse.TermItem(
                        term.getId(),
                        term.getCode(),
                        term.getTitle(),
                        term.getContent(),
                        term.getVersion(),
                        term.isRequired(),
                        term.isActive(),
                        term.getPublishedAt()))
                .toList();
        return new TermResponse(items);
    }

    @Transactional
    public ConsentTokenResponse saveConsents(ConsentRequest request, String ip, String userAgent) {
        if (request == null || request.consents() == null || request.consents().isEmpty()) {
            throw new TermsException(TermsErrorCode.CONSENT_EMPTY);
        }

        List<Term> activeTerms = termRepository.findAllByActiveTrue();
        Map<Long, Term> termMap = activeTerms.stream()
                .collect(Collectors.toMap(Term::getId, term -> term));

        LocalDateTime now = LocalDateTime.now();
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = now.plusHours(CONSENT_TOKEN_TTL_HOURS);

        Map<Long, ConsentRequest.ConsentItem> consentMap = request.consents().stream()
                .collect(Collectors.toMap(ConsentRequest.ConsentItem::termId, c -> c, (a, b) -> a));

        // Validate required terms are agreed and versions match.
        for (Term term : activeTerms) {
            ConsentRequest.ConsentItem consent = consentMap.get(term.getId());
            if (term.isRequired()) {
                if (consent == null || !consent.agreed()) {
                    throw new TermsException(TermsErrorCode.REQUIRED_TERMS_MISSING);
                }
                validateVersion(term, consent);
            } else if (consent != null && consent.agreed()) {
                validateVersion(term, consent);
            }
        }

        // Persist agreed items only.
        List<UserConsentPending> pendingList = request.consents().stream()
                .filter(ConsentRequest.ConsentItem::agreed)
                .map(consent -> {
                    Term term = termMap.get(consent.termId());
                    if (term == null || !term.isActive()) {
                        throw new TermsException(TermsErrorCode.TERM_NOT_ACTIVE);
                    }
                    validateVersion(term, consent);
                    return UserConsentPending.builder()
                            .consentToken(token)
                            .termId(term.getId())
                            .agreedVersion(consent.version())
                            .required(term.isRequired())
                            .agreedAt(now)
                            .ip(ip)
                            .userAgent(userAgent)
                            .status(ConsentStatus.VALID)
                            .expiresAt(expiresAt)
                            .build();
                })
                .toList();

        userConsentPendingRepository.saveAll(pendingList);
        return new ConsentTokenResponse(token);
    }

    private void validateVersion(Term term, ConsentRequest.ConsentItem consent) {
        if (!term.getVersion().equals(consent.version())) {
            throw new TermsException(TermsErrorCode.TERM_VERSION_MISMATCH);
        }
    }
}

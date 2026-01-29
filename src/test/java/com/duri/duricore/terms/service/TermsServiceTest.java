package com.duri.duricore.terms.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import com.duri.duricore.terms.dto.ConsentRequest;
import com.duri.duricore.terms.dto.ConsentRequest.ConsentItem;
import com.duri.duricore.terms.exception.TermsErrorCode;
import com.duri.duricore.terms.exception.TermsException;
import com.duri.duricore.terms.repository.TermRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TermsServiceTest {

    @Autowired
    private TermsService termsService;

    @Autowired
    private TermRepository termRepository;

    private Long requiredTermId;
    private Long optionalTermId;
    private String version;

    @BeforeEach
    void setUp() {
        var terms = termRepository.findAll();
        // assume seeded by initializer
        requiredTermId = terms.stream().filter(t -> t.isRequired()).findFirst().orElseThrow().getId();
        optionalTermId = terms.stream().filter(t -> !t.isRequired()).findFirst().orElseThrow().getId();
        version = terms.getFirst().getVersion();
    }

    @Test
    void throwsWhenRequiredTermMissing() {
        ConsentRequest request = new ConsentRequest(List.of(
                new ConsentItem(optionalTermId, version, true)
        ));

        assertThatThrownBy(() -> termsService.saveConsents(request, "127.0.0.1", "junit"))
                .isInstanceOf(TermsException.class)
                .extracting("errorCode")
                .isEqualTo(TermsErrorCode.REQUIRED_TERMS_MISSING);
    }

    @Test
    void returnsTokenWhenAllValid() {
        ConsentRequest request = new ConsentRequest(List.of(
                new ConsentItem(requiredTermId, version, true),
                new ConsentItem(optionalTermId, version, true)
        ));

        var response = termsService.saveConsents(request, "127.0.0.1", "junit");
        assertThat(response.consentToken()).isNotBlank();
    }
}

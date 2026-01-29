package com.duri.duricore.terms;

import com.duri.duricore.terms.entity.Term;
import com.duri.duricore.terms.repository.TermRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TermsDataInitializer {

    @Bean
    CommandLineRunner seedTerms(TermRepository termRepository) {
        return args -> {
            if (termRepository.count() > 0) {
                return;
            }
            LocalDateTime now = LocalDateTime.now();
            List<Term> seeds = List.of(
                    Term.builder()
                            .code("SERVICE")
                            .title("서비스 이용약관")
                            .content("서비스 이용약관 예시 텍스트입니다.")
                            .version("v1")
                            .required(true)
                            .active(true)
                            .publishedAt(now)
                            .build(),
                    Term.builder()
                            .code("PRIVACY")
                            .title("개인정보 처리방침")
                            .content("개인정보 처리방침 예시 텍스트입니다.")
                            .version("v1")
                            .required(true)
                            .active(true)
                            .publishedAt(now)
                            .build(),
                    Term.builder()
                            .code("MARKETING")
                            .title("마케팅 정보 수신 동의")
                            .content("마케팅 정보 수신 동의 예시 텍스트입니다.")
                            .version("v1")
                            .required(false)
                            .active(true)
                            .publishedAt(now)
                            .build());
            termRepository.saveAll(seeds);
        };
    }
}

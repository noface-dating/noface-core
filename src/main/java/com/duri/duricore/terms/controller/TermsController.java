package com.duri.duricore.terms.controller;

import com.duri.duricore.terms.dto.ConsentRequest;
import com.duri.duricore.terms.dto.ConsentTokenResponse;
import com.duri.duricore.terms.dto.TermResponse;
import com.duri.duricore.terms.service.TermsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/terms")
public class TermsController {

    private final TermsService termsService;

    public TermsController(TermsService termsService) {
        this.termsService = termsService;
    }

    @GetMapping
    public ResponseEntity<TermResponse> getTerms() {
        return ResponseEntity.ok(termsService.getActiveTerms());
    }

    @PostMapping("/consents")
    public ResponseEntity<ConsentTokenResponse> saveConsents(
            @RequestBody ConsentRequest request, HttpServletRequest servletRequest) {
        String ip = servletRequest.getRemoteAddr();
        String userAgent = servletRequest.getHeader("User-Agent");
        return ResponseEntity.ok(termsService.saveConsents(request, ip, userAgent));
    }
}

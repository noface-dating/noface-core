package com.duri.duricore.terms.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TermResponse(List<TermItem> terms) {

    public record TermItem(
            Long id,
            String code,
            String title,
            String content,
            String version,
            boolean required,
            boolean active,
            LocalDateTime publishedAt) {}
}

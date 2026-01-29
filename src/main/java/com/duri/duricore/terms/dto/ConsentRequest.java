package com.duri.duricore.terms.dto;

import java.util.List;

public record ConsentRequest(List<ConsentItem> consents) {

    public record ConsentItem(Long termId, String version, boolean agreed) {}
}

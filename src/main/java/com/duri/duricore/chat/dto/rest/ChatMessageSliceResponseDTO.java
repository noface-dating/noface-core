package com.duri.duricore.chat.dto.rest;

import java.util.List;

public record ChatMessageSliceResponseDTO(
        List<ChatMessageResponseDTO> messages,
        boolean hasNext
) {
}

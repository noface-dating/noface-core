package com.duri.duricore.chatting.dto;

import java.util.List;

public record ChatMessageSliceResponseDTO(
        List<ChatMessageResponseDTO> messages,
        boolean hasNext
) {
}

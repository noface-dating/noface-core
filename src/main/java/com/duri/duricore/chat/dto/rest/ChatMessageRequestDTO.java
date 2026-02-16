package com.duri.duricore.chat.dto.rest;

import java.util.UUID;

public record ChatMessageRequestDTO(
        UUID roomId,
        Long senderId,
        String content,
        String messageType,
        UUID messageId,
        UUID createdAt
) {}

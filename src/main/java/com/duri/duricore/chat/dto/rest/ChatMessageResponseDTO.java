package com.duri.duricore.chat.dto.rest;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageResponseDTO(
        UUID roomId,
        Long senderId,
        String senderNickname,
        String content,
        String messageType,
        UUID messageId,
        UUID createdAt,
        LocalDateTime timestamp
) {}

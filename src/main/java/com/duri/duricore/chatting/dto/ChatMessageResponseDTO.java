package com.duri.duricore.chatting.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageResponseDTO(
        Long roomId,
        Long senderId,
        String content,
        String messageType,
        UUID messageId,
        LocalDateTime timestamp
) {}

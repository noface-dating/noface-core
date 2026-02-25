package com.duri.duricore.chat.dto.websocket;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageSocketResponseDTO(
        UUID roomId,
        Long senderId,
        String content,
        String messageType,
        LocalDateTime createdAt
) {}

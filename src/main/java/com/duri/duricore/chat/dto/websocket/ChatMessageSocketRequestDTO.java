package com.duri.duricore.chat.dto.websocket;

import java.util.UUID;

public record ChatMessageSocketRequestDTO(
        UUID roomId,
        Long senderId,
        String content,
        String messageType
) {}

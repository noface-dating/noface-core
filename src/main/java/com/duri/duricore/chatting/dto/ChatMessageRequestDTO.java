package com.duri.duricore.chatting.dto;

import com.duri.duricore.chatting.entity.cassandra.MessageType;

import java.util.UUID;

public record ChatMessageRequestDTO(
        Long roomId,
        Long senderId,
        String content,
        String messageType,
        UUID messageId,
        UUID createdAt
) {}

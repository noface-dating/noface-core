package com.duri.duricore.chat.dto.rest;

import java.util.UUID;

public record ChatRoomSummaryResponseDTO(
        UUID roomId,
        String roomName,
        boolean messageCheck
) {}

package com.duri.duricore.chat.dto.rest;

import com.duri.duricore.chat.entity.mysql.RoomType;

public record CreateChatRoomRequestDTO(RoomType roomType, Long... userIds) {}

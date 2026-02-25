package com.duri.duricore.chat.repository.mysql;

import com.duri.duricore.chat.entity.mysql.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {}

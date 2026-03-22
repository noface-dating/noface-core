package com.duri.duricore.chat.entity.mysql;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * MariaDB chat_room_member ORM
 */
@Entity
@Table(
        name = "chat_room_member",
        indexes = {
                @Index(name = "idx_chat_room_member_user", columnList = "user_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember {

    @EmbeddedId
    private ChatRoomMemberId id;

    @MapsId("roomId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    public ChatRoomMember(ChatRoom chatRoom, Users users) {
        this.chatRoom = chatRoom;
        this.users = users;
        this.id = new ChatRoomMemberId(
                chatRoom.getRoomId(),
                users.getUserId()
        );
    }
}

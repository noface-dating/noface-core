package com.duri.duricore.chat.entity.mysql;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChatRoomMemberId implements Serializable {

    @Column(name = "chat_room_id", columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID roomId;

    @Column(name = "user_id")
    private Long userId;
}

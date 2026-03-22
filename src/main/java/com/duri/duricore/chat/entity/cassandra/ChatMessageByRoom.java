package com.duri.duricore.chat.entity.cassandra;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

/**
 * 카산드라 DB 채팅방 중심 메시지 조회 Entity ORM
 *
 * @author kyw10987
 */
@Getter
@NoArgsConstructor
@Table("chat_messages_by_room")
public class ChatMessageByRoom {

    @PrimaryKey
    private ChatMessageByRoomKey key;

    @Column("message_id")
    private UUID messageId;

    @Column("sender_id")
    private Long senderId;

    @Column("content")
    private String content;

    @Column("message_type")
    private MessageType messageType;

    public ChatMessageByRoom(
            UUID roomId,
            Long senderId,
            String content,
            MessageType messageType
    ) {
        this.key = new ChatMessageByRoomKey(roomId, Uuids.timeBased());
        this.messageId = UUID.randomUUID();
        this.senderId = senderId;
        this.content = content;
        this.messageType = messageType;
    }
}

package com.duri.duricore.chat.entity.cassandra;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.Column;

import java.util.UUID;

/**
 * 카산드라 DB 사용자 중심 메시지 조회 Entity ORM
 *
 * @author kyw10987
 */
@Getter
@NoArgsConstructor
@Table("chat_messages_by_sender")
public class ChatMessageBySender {

    @PrimaryKey
    private ChatMessageBySenderKey key;

    @Column("room_id")
    private UUID roomId;

    @Column("message_id")
    private UUID messageId;

    @Column("content")
    private String content;

    @Column("message_type")
    private MessageType messageType;

    public ChatMessageBySender(
            Long senderId,
            UUID roomId,
            UUID messageId,
            String content,
            MessageType messageType
    ) {
        this.key = new ChatMessageBySenderKey(senderId, Uuids.timeBased());
        this.roomId = roomId;
        this.messageId = messageId;
        this.content = content;
        this.messageType = messageType;
    }
}

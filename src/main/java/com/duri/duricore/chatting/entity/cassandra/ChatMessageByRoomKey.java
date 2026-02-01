package com.duri.duricore.chatting.entity.cassandra;

import lombok.*;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@PrimaryKeyClass
public class ChatMessageByRoomKey implements Serializable {

    @PrimaryKeyColumn(
            name = "room_id",
            type = PrimaryKeyType.PARTITIONED
    )
    private Long roomId;

    @PrimaryKeyColumn(
            name = "created_at",
            type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING
    )
    @CassandraType(type = CassandraType.Name.TIMEUUID)
    private UUID createdAt; // TIMEUUID -> UUID
}

package com.duri.duricore.chatting.entity.cassandra;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class ChatMessageBySenderKey implements Serializable {

    @PrimaryKeyColumn(
            name = "sender_id",
            type = PrimaryKeyType.PARTITIONED
    )
    private Long senderId;

    @PrimaryKeyColumn(
            name = "created_at",
            type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING
    )
    @CassandraType(type = CassandraType.Name.TIMEUUID)
    private UUID createdAt;
}

package com.duri.duricore.chat.repository.cassandra;

import com.duri.duricore.chat.entity.cassandra.ChatMessageByRoom;
import com.duri.duricore.chat.entity.cassandra.ChatMessageByRoomKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatMessageByRoomRepository extends CassandraRepository<ChatMessageByRoom, ChatMessageByRoomKey> {

    Slice<ChatMessageByRoom> findByKeyRoomId(UUID roomId, Pageable pageable);

    Slice<ChatMessageByRoom> findByKeyRoomIdAndKeyCreatedAtLessThan(UUID roomId, UUID createdAt, Pageable pageable);
}


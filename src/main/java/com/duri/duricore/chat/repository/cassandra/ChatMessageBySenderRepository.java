package com.duri.duricore.chat.repository.cassandra;

import com.duri.duricore.chat.entity.cassandra.ChatMessageBySender;
import com.duri.duricore.chat.entity.cassandra.ChatMessageBySenderKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageBySenderRepository extends CassandraRepository<ChatMessageBySender, ChatMessageBySenderKey> {

    Slice<ChatMessageBySender> findBykeySenderId(
            Long senderId,
            Pageable pageable
    );
}

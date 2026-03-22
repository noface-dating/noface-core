package com.duri.duricore.chat.repository.mysql;

import com.duri.duricore.chat.entity.mysql.ChatRoom;
import com.duri.duricore.chat.entity.mysql.ChatRoomMember;
import com.duri.duricore.chat.entity.mysql.ChatRoomMemberId;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomMemberRepository
        extends JpaRepository<ChatRoomMember, ChatRoomMemberId> {

    // 채팅방에 특정 멤버 존재 여부
    boolean existsById(@NonNull ChatRoomMemberId chatRoomMemberId);

    // 채팅방 멤버 삭제
    void deleteById(@NonNull ChatRoomMemberId chatRoomMemberId);

    // 멤버가 속한 채팅방 조회
    List<ChatRoomMember> findRoomIdsByIdUserId(Long userId);

    // 채팅방 멤버수 조회
    int countByChatRoom(ChatRoom chatRoom);

    // 채팅방 멤버 조회
    List<ChatRoomMember> findAllByChatRoom(ChatRoom chatRoom);
}

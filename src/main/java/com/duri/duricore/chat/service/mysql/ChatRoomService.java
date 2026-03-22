package com.duri.duricore.chat.service.mysql;

import com.duri.duricore.chat.dto.rest.ChatRoomSummaryResponseDTO;
import com.duri.duricore.chat.entity.mysql.*;
import com.duri.duricore.chat.repository.mysql.ChatRoomMemberRepository;
import com.duri.duricore.chat.repository.mysql.ChatRoomRepository;
import com.duri.duricore.chat.repository.mysql.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UsersRepository usersRepository;

    //채팅방 생성 & 멤버 추가
    public UUID createChatRoom(RoomType roomType, Long... userIds) {

        UUID roomId = UUID.randomUUID();
        ChatRoom chatRoom = new ChatRoom(roomId, roomType);
        chatRoomRepository.save(chatRoom);

        List<Users> users = usersRepository.findAllByUsersId(userIds);

        List<ChatRoomMember> members = users.stream()
                .map(user -> new ChatRoomMember(chatRoom, user))
                .toList();

        chatRoomMemberRepository.saveAll(members);

        return roomId;
    }

    // 채팅방에 멤버 추가
    public void addMember(UUID roomId, Long userId) {

        // 멤버 중복 추가 체크
        if (chatRoomMemberRepository.existsById(new ChatRoomMemberId(roomId, userId))) {
            // exception 추가
            return;
        }

        chatRoomMemberRepository.save(
                new ChatRoomMember(
                        chatRoomRepository.findById(roomId).orElseThrow(),
                        usersRepository.findById(userId).orElseThrow()
                )
        );
    }

    // 멤버 제거 + 방 관리 방에 멤버가 없으면 방 제거
    public void removeMember(UUID roomId, Long userId) {
        if (!chatRoomMemberRepository.existsById(new ChatRoomMemberId(roomId, userId))) {
            //exception 추가
            return;
        }

        chatRoomMemberRepository.deleteById(new ChatRoomMemberId(roomId, userId));

        int count = chatRoomMemberRepository.countByChatRoom(
                chatRoomRepository.findById(roomId).get()
        );
        if (count == 0) {
            chatRoomRepository.deleteById(roomId);
        }
    }

    // 접근 권한 체크
    @Transactional(readOnly = true)
    public void hasAccess(UUID roomId, Long userId) {
        chatRoomMemberRepository.existsById(new ChatRoomMemberId(roomId, userId));// exception 추가
    }

    // 유저가 속한 채팅방 목록
    @Transactional(readOnly = true)
    public List<ChatRoomSummaryResponseDTO> findRoomsByUser(Long userId) {
        List<ChatRoomMember> chatRoomMembers =
                chatRoomMemberRepository.findRoomIdsByIdUserId(userId);

        return chatRoomMembers.stream()
                .map(member -> {
                    ChatRoom chatRoom = member.getChatRoom();

                    List<ChatRoomMember> members =
                            chatRoomMemberRepository.findAllByChatRoom(chatRoom);

                    String roomName = members.stream()
                            .map(ChatRoomMember::getUsers)
                            .filter(users -> !users.getUserId().equals(userId))
                            .map(Users::getUsername)
                            .sorted()
                            .collect(Collectors.joining(", "));

                    return new ChatRoomSummaryResponseDTO(
                            chatRoom.getRoomId(),
                            roomName,
                            true //messageCheck 임시
                    );
                })
                .toList();
    }

    // 채팅방 유저 목록
    public Map<Long, String> chatRoomUsersList(UUID roomId) {

        List<ChatRoomMember> roomMembers = chatRoomMemberRepository
                .findAllByChatRoom(chatRoomRepository.findById(roomId).get());

        Map<Long, String> users = new HashMap<>();
        roomMembers.forEach(member -> users.put(
                member.getUsers().getUserId(),
                member.getUsers().getUsername())
        );

        return users;
    }
}

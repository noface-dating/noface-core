package com.duri.duricore.chat.controller;

import com.duri.duricore.chat.dto.rest.ChatRoomSummaryResponseDTO;
import com.duri.duricore.chat.dto.rest.CreateChatRoomRequestDTO;
import com.duri.duricore.chat.repository.mysql.UsersRepository;
import com.duri.duricore.chat.service.mysql.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 채팅방 CRUD
 * ++ 인증쪽 구현 완료 이후 수정 필요.
 *
 * @author kyw10987
 */
@RestController
@RequestMapping("api/v1/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    private final UsersRepository usersRepository;

    /**
     * 사용자가 속한 채팅방을 조회
     *
     * @return 사용자가 속해있는 채팅방 리스트
     */
    @GetMapping
    public ResponseEntity<List<ChatRoomSummaryResponseDTO>>  getMyChatRooms() {
        // for test
        Long userId = 1L;
        return ResponseEntity.ok(chatRoomService.findRoomsByUser(userId));
    }

    /**
     * 채팅방 생성
     *
     * @param createChatRoomRequestDTO 방타입(DM/Group)과 속한 유저들의 Id가 들어있는 가변리스트가 있는 DTO
     * @return 생성완료 -> (201 CREATED)
     */
    @PostMapping
    public ResponseEntity<UUID> createChatRoom(@RequestBody CreateChatRoomRequestDTO createChatRoomRequestDTO) {

        UUID roomId = chatRoomService.createChatRoom(
                createChatRoomRequestDTO.roomType(),
                createChatRoomRequestDTO.userIds()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(roomId);
    }

    /**
     * 채팅방에 멤버 추가
     *
     * @param roomId roomId uniqueID
     * @param userId 추가하려는 사용자ID
     * @return 추가성공 -> (201 CREATED)
     */
    @PostMapping("/{roomId}/members/{userId}")
    public ResponseEntity<Void> addMember(
            @PathVariable UUID roomId,
            @PathVariable Long userId
    ) {
        chatRoomService.addMember(roomId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 채팅방 멤버 제거
     *
     * @param roomId target 채팅방
     * @param userId target 유저
     * @return 제거완료 -> (204 NO_CONTENT)
     */
    @DeleteMapping("/{roomId}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable UUID roomId,
            @PathVariable Long userId
    ) {
        chatRoomService.removeMember(roomId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

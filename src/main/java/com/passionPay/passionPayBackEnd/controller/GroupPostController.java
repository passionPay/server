package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.GroupPostDto;
import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.GroupPostRequestDto;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupPost;
import com.passionPay.passionPayBackEnd.service.GroupPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupPostController {

    private final GroupPostService groupPostService;

    public GroupPostController(GroupPostService groupPostService) {
        this.groupPostService = groupPostService;
    }

    // group 에 post 생성
    @PostMapping("/{groupId}/post")
    public ResponseEntity<GroupPostDto> saveGroupPost(
            @PathVariable(name="groupId") Long groupId, @RequestBody GroupPostRequestDto groupPostRequestDto) {
        return ResponseEntity.ok(groupPostService.save(groupPostRequestDto, groupId));
    }

    // group post 전부 조회
    @GetMapping("/{groupId}/post")
    public ResponseEntity<List<GroupPostDto>> getAllGroupPost(@PathVariable(name="groupId") Long groupId) {
        return ResponseEntity.ok(groupPostService.getGroupPostByGroupId(groupId));
    }

    // group post 업데이트
    @PutMapping("/post/{groupPostId}")
    public ResponseEntity<GroupPostDto> updateGroupPost(
            @PathVariable(name="groupPostId") Long groupPostId, @RequestBody GroupPostRequestDto groupPostRequestDto) {
        return ResponseEntity.ok(groupPostService.update(groupPostRequestDto, groupPostId));
    }

    // group post 삭제
    @DeleteMapping("/post/{groupPostId}")
    public ResponseEntity<Boolean> deleteGroupPost(@PathVariable(name="groupPostId") Long groupPostId) {
        return ResponseEntity.ok(groupPostService.delete(groupPostId));
    }
}

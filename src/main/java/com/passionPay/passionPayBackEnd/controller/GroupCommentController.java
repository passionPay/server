package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.GroupCommentDto;
import com.passionPay.passionPayBackEnd.service.GroupCommentService;
import com.passionPay.passionPayBackEnd.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupCommentController {
    private final GroupCommentService groupCommentService;

    public GroupCommentController(GroupCommentService groupCommentService) {
        this.groupCommentService = groupCommentService;
    }

    // save comment
    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<GroupCommentDto> save(
            @PathVariable("postId") Long postId, @RequestBody String content) {
        return ResponseEntity.ok(groupCommentService.save(postId, content, SecurityUtil.getCurrentMemberId()));
    }

    // find comments by post id
    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<List<GroupCommentDto>> findAllByPostId(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(groupCommentService.getGroupCommentByPostId(postId));
    }

    // update comment
    @PutMapping("/post/comment/{commentId}")
    public ResponseEntity<GroupCommentDto> updateComment(
            @PathVariable("commentId") Long commentId, @RequestBody String content) {
        return ResponseEntity.ok(groupCommentService.updateGroupComment(commentId, content));
    }

    // delete comment
    @DeleteMapping("/post/comment/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(groupCommentService.delete(commentId));
    }
}

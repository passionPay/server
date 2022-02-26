package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.service.GroupLikeService;
import com.passionPay.passionPayBackEnd.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
public class GroupLikeController {
    private final GroupLikeService groupLikeService;

    public GroupLikeController(GroupLikeService groupLikeService) {
        this.groupLikeService = groupLikeService;
    }

    // save
    @PostMapping("/post/{postId}/like")
    public ResponseEntity<Long> save(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(groupLikeService.save(SecurityUtil.getCurrentMemberId(), postId));
    }

    // get count
    @GetMapping("/post/{postId}/like/count")
    public ResponseEntity<Long> getCountByPostId(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(groupLikeService.getLikeCountByPostId(postId));
    }

    // delete
    @DeleteMapping("/post/like/{likeId}")
    public ResponseEntity<Boolean> delete(@PathVariable("likeId") Long likeId) {
        return ResponseEntity.ok(groupLikeService.delete(likeId));
    }
}

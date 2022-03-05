package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.FollowInfoDto;
import com.passionPay.passionPayBackEnd.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{userId}/{followerId}")
    public ResponseEntity<FollowInfoDto> addFollower(@PathVariable(name = "userId") Long userId, @PathVariable(name = "followerId") Long followerId) {
        return ResponseEntity.ok(followService.addFollow(userId, followerId));
    }


    @GetMapping("/following/{memberId}")
    public ResponseEntity<List<FollowInfoDto> > getFollowing(@PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(followService.getFollowing(memberId));
    }

    @GetMapping("/follower/{memberId}")
    public ResponseEntity<List<FollowInfoDto> > getFollower(@PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(followService.getFollower(memberId));
    }


    @GetMapping("/request/ob/{memberId}")
    public ResponseEntity<List<FollowInfoDto> > getFollowingOBRequest(@PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(followService.getFollowingOBRequest(memberId));
    }

    @GetMapping("/request/ib/{memberId}")
    public ResponseEntity<List<FollowInfoDto> > getFollowingIBRequest(@PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(followService.getFollowingIBRequest(memberId));
    }

    @PutMapping("/validate/{followId}")
    public ResponseEntity<Boolean> acceptFollowRequest(@PathVariable(name = "followId") Long followId) {
        return ResponseEntity.ok(followService.acceptFollowRequest(followId));
    }

    @DeleteMapping("/{followId}")
    public ResponseEntity<Long> deleteUser(@PathVariable(name = "followId") Long followId) {
        return ResponseEntity.ok(followService.deleteFollow(followId));
    }

}

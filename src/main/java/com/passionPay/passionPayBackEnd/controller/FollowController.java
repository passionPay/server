package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.FollowInfoDto;
import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import com.passionPay.passionPayBackEnd.domain.Follow;
import com.passionPay.passionPayBackEnd.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PutMapping("/{userId}/{followerId}")
    public ResponseEntity<FollowInfoDto> addFollower(@PathVariable(name = "userId") Long userId, @PathVariable(name = "followerId") Long followerId) {
        return ResponseEntity.ok(followService.addFollow(userId, followerId));
    }

    @DeleteMapping("/{followId}")
    public ResponseEntity<Long> deleteUser(@PathVariable(name = "followId") Long followId) {
        return ResponseEntity.ok(followService.deleteFollow(followId));
//        return ResponseEntity.notFound();
    }

    @GetMapping("/{memberId}/following")
    public ResponseEntity<Optional<List<FollowInfoDto> > > getFollowing(@PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(followService.getFollowing(memberId));
    }

    @GetMapping("/{memberId}/follower")
    public ResponseEntity<Optional<List<FollowInfoDto> > > getFollower(@PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(followService.getFollower(memberId));
    }

}

package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.PaginationInfoDto;
import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostDto;
import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostInfoDto;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateCommunityType;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePost;
import com.passionPay.passionPayBackEnd.service.PrivateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/private")
public class PrivateController {

    private final PrivateService privateService;

    @Autowired
    public PrivateController(PrivateService privateService) {
        this.privateService = privateService;
    }

//    @PostMapping("/community")
//    public ResponseEntity<Long> addCommunity(@RequestBody PrivateCommunityDto privateCommunityDto) {
//        return ResponseEntity.ok(privateService.addCommunity(privateCommunityDto));
//    }

    @PostMapping("/post")
    public ResponseEntity<Long> addPost(@RequestBody PrivatePostDto privatePostDto) {
        return ResponseEntity.ok(privateService.addPost(privatePostDto));
    }


    @GetMapping("/{schoolName}/{communityType}")
    public ResponseEntity<List<PrivatePostInfoDto> > getPostBySchoolAndCommunity(
            @PathVariable(name = "schoolName") String schoolName,
            @PathVariable(name = "communityType") PrivateCommunityType communityType,
            @RequestBody PaginationInfoDto paginationInfoDto)
    {
        return ResponseEntity.ok(privateService.getPostBySchoolAndCommunity(schoolName, communityType, paginationInfoDto.getPageSize(), paginationInfoDto.getPageNumber()));
    }

    @GetMapping("/{memberId}/post")
    public ResponseEntity<List<PrivatePostInfoDto> > getPostByMember(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody PaginationInfoDto paginationInfoDto)
    {
        return ResponseEntity.ok(privateService.getPostByMember(memberId, paginationInfoDto.getPageSize(), paginationInfoDto.getPageNumber()));
    }

    @GetMapping("{communityType}/{memberId}/post")
    public ResponseEntity<List<PrivatePostInfoDto> > getPostByCommunityAndMember(
            @PathVariable(name = "communityType") PrivateCommunityType communityType,
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody PaginationInfoDto paginationInfoDto)
    {
        return ResponseEntity.ok(privateService.getPostByCommunityAndMember(communityType, memberId, paginationInfoDto.getPageSize(), paginationInfoDto.getPageNumber()));
    }

    @PostMapping("{postId}/{memberId}/like")
    public ResponseEntity<Long> addPostLike(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "memberId") Long memberId)
    {
        return ResponseEntity.ok(privateService.addPostLike(postId, memberId));
    }

    @DeleteMapping("{postId}/{memberId}/like")
    public ResponseEntity<Long> deletePostLike(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "memberId") Long memberId)
    {
        return ResponseEntity.ok(privateService.deletePostLike(postId, memberId));
    }

    @GetMapping("{postId}/{memberId}/like")
    public ResponseEntity<Boolean> isUserLikesPost(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "memberId") Long memberId)
    {
        return ResponseEntity.ok(privateService.isUserLikesPost(postId, memberId));
    }


    @GetMapping("{postId}/like")
    public ResponseEntity<Integer> likeCountOfPost(@PathVariable(name = "postId") Long postId) {
        return ResponseEntity.ok(privateService.likeCountOfPost(postId));
    }






//    @GetMapping("/{communityName}")
//    public ResponseEntity<MemberInfoDto> getUserInfo(@PathVariable(name = "communityName") String communityName) {
//        return ResponseEntity.ok();
//    }
}

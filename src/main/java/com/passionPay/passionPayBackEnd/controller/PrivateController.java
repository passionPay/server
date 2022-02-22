package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.PaginationInfoDto;
import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivateCommentDto;
import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivateCommentInfoDto;
import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostDto;
import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostInfoDto;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateComment;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateCommunityType;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePost;
import com.passionPay.passionPayBackEnd.service.PrivateService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
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

    /*
     * 게시글 관련
     */

    @PostMapping("/post")
    public ResponseEntity<Long> addPost(@RequestBody PrivatePostDto privatePostDto) {
        return ResponseEntity.ok(privateService.addPost(privatePostDto));
    }

    @GetMapping("/post/{memberId}")
    public ResponseEntity<List<PrivatePostInfoDto> > getPostByMember(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody PaginationInfoDto paginationInfoDto)
    {
        System.out.println("post by memberId: " + memberId);
        System.out.println(paginationInfoDto.getPageSize() +  " " + paginationInfoDto.getPageNumber());
        return ResponseEntity.ok(privateService.getPostByMember(memberId, paginationInfoDto.getPageSize(), paginationInfoDto.getPageNumber()));
    }

    @GetMapping("/post/schoolCommunity/{schoolName}/{communityType}")
    public ResponseEntity<List<PrivatePostInfoDto> > getPostBySchoolAndCommunity(
            @PathVariable(name = "schoolName") String schoolName,
            @PathVariable(name = "communityType") PrivateCommunityType communityType,
            @RequestBody PaginationInfoDto paginationInfoDto)
    {
        System.out.println("post by school and community");
        return ResponseEntity.ok(privateService.getPostBySchoolAndCommunity(schoolName, communityType, paginationInfoDto.getPageSize(), paginationInfoDto.getPageNumber()));
    }

    @GetMapping("/post/communityMember/{communityType}/{memberId}")
    public ResponseEntity<List<PrivatePostInfoDto> > getPostByCommunityAndMember(
            @PathVariable(name = "communityType") PrivateCommunityType communityType,
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody PaginationInfoDto paginationInfoDto)
    {
        return ResponseEntity.ok(privateService.getPostByCommunityAndMember(communityType, memberId, paginationInfoDto.getPageSize(), paginationInfoDto.getPageNumber()));
    }

    @GetMapping("/post/count/{memberId}")
    public ResponseEntity<Integer> getPostCountByMember(@PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(privateService.getPostCountByMember(memberId));
    }

    @GetMapping("/post/comment/{memberId}")
    public ResponseEntity<List<PrivatePostInfoDto> > getPostByMemberComment(@PathVariable(name = "memberId") Long memberId) {
//        System.out.println("getPostByMemberComment");
        return ResponseEntity.ok(privateService.getPostByMemberComment(memberId));
    }

    /*
     * 게시글 좋아요 기능
     */

    @PostMapping("/like/{postId}/{memberId}")
    public ResponseEntity<Long> addPostLike(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "memberId") Long memberId)
    {
        return ResponseEntity.ok(privateService.addPostLike(postId, memberId));
    }

    @DeleteMapping("/like/{postId}/{memberId}")
    public ResponseEntity<Long> deletePostLike(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "memberId") Long memberId)
    {
        return ResponseEntity.ok(privateService.deletePostLike(postId, memberId));
    }

    @GetMapping("/like/{postId}/{memberId}")
    public ResponseEntity<Boolean> isUserLikesPost(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "memberId") Long memberId)
    {
        return ResponseEntity.ok(privateService.isUserLikesPost(postId, memberId));
    }


    @GetMapping("/like/{postId}")
    public ResponseEntity<Integer> likeCountOfPost(@PathVariable(name = "postId") Long postId) {
        return ResponseEntity.ok(privateService.likeCountOfPost(postId));
    }



    /*
     * 댓글 기능
     */

    @PostMapping("/comment")
    public ResponseEntity<Long> addComment(@RequestBody PrivateCommentDto privateCommentDto) {
        return ResponseEntity.ok(privateService.addComment(privateCommentDto));
    }


    @GetMapping("/comment/{postId}/{memberId}")
    public ResponseEntity<List<PrivateCommentInfoDto> > getCommentByPostAndMember(@PathVariable(name = "postId") Long postId , @PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(privateService.getCommentByPostAndMember(postId, memberId));
    }


    @GetMapping("/comment/{memberId}/post/count")
    public ResponseEntity<Long> getNumPostOfCommented(@PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(privateService.getNumPostOfCommented(memberId));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Integer> deleteComment(@PathVariable(name = "commentId") Long commentId) {
        return ResponseEntity.ok(privateService)
    }

    /*
     * 댓글 좋야요 기능
     */

    @PostMapping("/{commentId}/{memberId}/comment/like")
    public ResponseEntity<Long> addCommentLike(@PathVariable(name = "commentId") Long commentId, @PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(privateService.addCommentLike(commentId, memberId));
    }

    @DeleteMapping("/{commentId}/{memberId}/comment/like")
    public ResponseEntity<Long> deleteCommentLike(@PathVariable(name = "commentId") Long commentId, @PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(privateService.deleteCommentLike(commentId, memberId));
    }

    @GetMapping("/{commentId}/comment/like")
    public ResponseEntity<Integer> getLikeByComment(@PathVariable(name = "commentId") Long commentId) {
        return ResponseEntity.ok(privateService.getLikeByComment(commentId));
    }

    @GetMapping("/{commentId}/{memberId}/comment/like")
    public ResponseEntity<Boolean> getLikeByComment(@PathVariable(name = "commentId") Long commentId, @PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(privateService.isCommentLikedByMember(commentId, memberId));
    }

}

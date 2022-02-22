package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.PaginationInfoDto;
import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.*;
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

    //게시글 추가
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

    @PutMapping("/post/{postId}")
    public ResponseEntity<Integer> modifyPost(@PathVariable(name = "postId") Long postId, @RequestBody PrivatePostModifyDto privatePostModifyDto) {
        return ResponseEntity.ok(privateService.modifyPost(postId, privatePostModifyDto));
    }

    //유저가 쓴 게시글 개수
    @GetMapping("/post/count/{memberId}")
    public ResponseEntity<Integer> getPostCountByMember(@PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(privateService.getPostCountByMember(memberId));
    }

    //유저의 댓글이 있는 게시글을 가져옴
    @GetMapping("/post/comment/{memberId}")
    public ResponseEntity<List<PrivatePostInfoDto> > getPostByMemberComment(@PathVariable(name = "memberId") Long memberId) {
//        System.out.println("getPostByMemberComment");
        return ResponseEntity.ok(privateService.getPostByMemberComment(memberId));
    }

    //댓글이 있는 게시글의 개수
    @GetMapping("post/comment/{memberId}/count")
    public ResponseEntity<Long> getNumPostOfCommented(@PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(privateService.getNumPostOfCommented(memberId));
    }

    /*
     * 게시글 좋아요 기능
     */

    @PostMapping("/post/like/{postId}/{memberId}")
    public ResponseEntity<Long> addPostLike(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "memberId") Long memberId)
    {
        return ResponseEntity.ok(privateService.addPostLike(postId, memberId));
    }

    @DeleteMapping("/post/like/{postId}/{memberId}")
    public ResponseEntity<Long> deletePostLike(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "memberId") Long memberId)
    {
        return ResponseEntity.ok(privateService.deletePostLike(postId, memberId));
    }

    @GetMapping("/post/like/{postId}/{memberId}")
    public ResponseEntity<Boolean> isUserLikesPost(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "memberId") Long memberId)
    {
        return ResponseEntity.ok(privateService.isUserLikesPost(postId, memberId));
    }


    @GetMapping("/post/like/{postId}")
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

//    //댓글이 있는 게시글의 개수 가져옴
//    @GetMapping("/comment/{memberId}/post/count")
//    public ResponseEntity<Long> getNumPostOfCommented(@PathVariable(name = "memberId") Long memberId) {
//        return ResponseEntity.ok(privateService.getNumPostOfCommented(memberId));
//    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Integer> deleteComment(@PathVariable(name = "commentId") Long commentId) {
        return ResponseEntity.ok(privateService.deleteComment(commentId));
    }

    @PutMapping("comment/{commentId}")
    public ResponseEntity<Integer> modifyComment(@PathVariable(name = "commentId") Long commentId, @RequestBody PrivateCommentModifyDto privateCommentModifyDto) {
        return ResponseEntity.ok(privateService.modifyComment(commentId, privateCommentModifyDto.getContent()));
    }

    /*
     * 댓글 좋아요 기능
     */

    @PostMapping("/comment/like/{commentId}/{memberId}")
    public ResponseEntity<Long> addCommentLike(@PathVariable(name = "commentId") Long commentId, @PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(privateService.addCommentLike(commentId, memberId));
    }

    @DeleteMapping("/comment/like/{commentId}/{memberId}")
    public ResponseEntity<Long> deleteCommentLike(@PathVariable(name = "commentId") Long commentId, @PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(privateService.deleteCommentLike(commentId, memberId));
    }

    @GetMapping("/comment/like/{commentId}")
    public ResponseEntity<Integer> getLikeByComment(@PathVariable(name = "commentId") Long commentId) {
        return ResponseEntity.ok(privateService.getLikeByComment(commentId));
    }

    @GetMapping("/comment/like/{commentId}/{memberId}")
    public ResponseEntity<Boolean> isCommentLikedByMember(@PathVariable(name = "commentId") Long commentId, @PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(privateService.isCommentLikedByMember(commentId, memberId));
    }



}

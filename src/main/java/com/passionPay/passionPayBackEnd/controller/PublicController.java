package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.PaginationInfoDto;
import com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto.*;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicCommunityType;
import com.passionPay.passionPayBackEnd.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final PublicService publicService;

    @Autowired
    public PublicController(PublicService publicService) {
        this.publicService = publicService;
    }

    /*
     * 게시글 관련
     */

    //게시글 추가
    @PostMapping("/post")
    public ResponseEntity<Long> addPost(@RequestBody PublicPostDto publicPostDto) {
        return ResponseEntity.ok(publicService.addPost(publicPostDto));
    }

    @GetMapping("/post/{memberId}")
    public ResponseEntity<List<PublicPostInfoDto> > getPostByMember(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody PaginationInfoDto paginationInfoDto)
    {
        return ResponseEntity.ok(publicService.getPostByMember(memberId, paginationInfoDto.getPageSize(), paginationInfoDto.getPageNumber()));
    }

    @GetMapping("/post/community/{communityType}")
    public ResponseEntity<List<PublicPostInfoDto> > getPostByCommunity(
            @PathVariable(name = "communityType") PublicCommunityType communityType,
            @RequestBody PaginationInfoDto paginationInfoDto)
    {
        return ResponseEntity.ok(publicService.getPostByCommunity(communityType, paginationInfoDto.getPageSize(), paginationInfoDto.getPageNumber()));
    }

    @GetMapping("/post/communityMember/{communityType}/{memberId}")
    public ResponseEntity<List<PublicPostInfoDto> > getPostByCommunityAndMember(
            @PathVariable(name = "communityType") PublicCommunityType communityType,
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody PaginationInfoDto paginationInfoDto)
    {
        return ResponseEntity.ok(publicService.getPostByCommunityAndMember(communityType, memberId, paginationInfoDto.getPageSize(), paginationInfoDto.getPageNumber()));
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<Integer> modifyPost(@PathVariable(name = "postId") Long postId, @RequestBody PublicPostModifyDto publicPostModifyDto) {
        return ResponseEntity.ok(publicService.modifyPost(postId, publicPostModifyDto));
    }

    //유저가 쓴 게시글 개수
    @GetMapping("/post/count/{memberId}")
    public ResponseEntity<Integer> getPostCountByMember(@PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(publicService.getPostCountByMember(memberId));
    }

    //유저의 댓글이 있는 게시글을 가져옴
    @GetMapping("/post/comment/{memberId}")
    public ResponseEntity<List<PublicPostInfoDto> > getPostByMemberComment(@PathVariable(name = "memberId") Long memberId) {
//        System.out.println("getPostByMemberComment");
        return ResponseEntity.ok(publicService.getPostByMemberComment(memberId));
    }

    //댓글이 있는 게시글의 개수
    @GetMapping("post/comment/{memberId}/count")
    public ResponseEntity<Long> getNumPostOfCommented(@PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(publicService.getNumPostOfCommented(memberId));
    }

    @DeleteMapping("post/{postId}")
    public ResponseEntity<Integer> deletePost(@PathVariable(name = "postId") Long postId) {
        return ResponseEntity.ok(publicService.deletePost(postId));
    }


    /*
     * 게시글 좋아요 기능
     */

    @PostMapping("/post/like/{postId}/{memberId}")
    public ResponseEntity<Long> addPostLike(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "memberId") Long memberId)
    {
        return ResponseEntity.ok(publicService.addPostLike(postId, memberId));
    }

    @DeleteMapping("/post/like/{postId}/{memberId}")
    public ResponseEntity<Long> deletePostLike(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "memberId") Long memberId)
    {
        return ResponseEntity.ok(publicService.deletePostLike(postId, memberId));
    }

    @GetMapping("/post/like/{postId}/{memberId}")
    public ResponseEntity<Boolean> isUserLikesPost(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "memberId") Long memberId)
    {
        return ResponseEntity.ok(publicService.isUserLikesPost(postId, memberId));
    }


    @GetMapping("/post/like/{postId}")
    public ResponseEntity<Integer> likeCountOfPost(@PathVariable(name = "postId") Long postId) {
        return ResponseEntity.ok(publicService.likeCountOfPost(postId));
    }



    /*
     * 댓글 기능
     */

    @PostMapping("/comment")
    public ResponseEntity<Long> addComment(@RequestBody PublicCommentDto publicCommentDto) {
        return ResponseEntity.ok(publicService.addComment(publicCommentDto));
    }


    @GetMapping("/comment/{postId}/{memberId}")
    public ResponseEntity<List<PublicCommentInfoDto> > getCommentByPostAndMember(@PathVariable(name = "postId") Long postId , @PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(publicService.getCommentByPostAndMember(postId, memberId));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Integer> deleteComment(@PathVariable(name = "commentId") Long commentId) {
        return ResponseEntity.ok(publicService.deleteComment(commentId));
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Integer> modifyComment(@PathVariable(name = "commentId") Long commentId, @RequestBody PublicCommentModifyDto publicCommentModifyDto) {
        return ResponseEntity.ok(publicService.modifyComment(commentId, publicCommentModifyDto.getContent()));
    }

    /*
     * 댓글 좋아요 기능
     */

    @PostMapping("/comment/like/{commentId}/{memberId}")
    public ResponseEntity<Long> addCommentLike(@PathVariable(name = "commentId") Long commentId, @PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(publicService.addCommentLike(commentId, memberId));
    }

    @DeleteMapping("/comment/like/{commentId}/{memberId}")
    public ResponseEntity<Long> deleteCommentLike(@PathVariable(name = "commentId") Long commentId, @PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(publicService.deleteCommentLike(commentId, memberId));
    }

    @GetMapping("/comment/like/{commentId}")
    public ResponseEntity<Integer> getLikeByComment(@PathVariable(name = "commentId") Long commentId) {
        return ResponseEntity.ok(publicService.getLikeByComment(commentId));
    }

    @GetMapping("/comment/like/{commentId}/{memberId}")
    public ResponseEntity<Boolean> isCommentLikedByMember(@PathVariable(name = "commentId") Long commentId, @PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(publicService.isCommentLikedByMember(commentId, memberId));
    }

}

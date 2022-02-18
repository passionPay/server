package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.*;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateComment;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateCommunityType;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePost;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePostLike;
import com.passionPay.passionPayBackEnd.repository.MemberRepository;
import com.passionPay.passionPayBackEnd.repository.PrivateCommentRepository;
import com.passionPay.passionPayBackEnd.repository.PrivatePostLikeRepository;
import com.passionPay.passionPayBackEnd.repository.PrivatePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateService {

    private final MemberRepository memberRepository;
    private final PrivatePostRepository privatePostRepository;
    private final PrivatePostLikeRepository privatePostLikeRepository;
    private final PrivateCommentRepository privateCommentRepository;

//    @Transactional
//    public Long addCommunity(PrivateCommunityDto privateCommunityDto) {
//
//        if(privateCommunityRepository.existsByCommunityNameAndCommunityType(privateCommunityDto.getCommunityName(), privateCommunityDto.getCommunityType())) {
//            throw new RuntimeException("existing community!");
//        }
//        else {
//            PrivateCommunity privateCommunity = privateCommunityDto.toPrivateCommunity();
//            privateCommunityRepository.save(privateCommunity);
//            return privateCommunity.getId();
//        }
//
//    }

    /*
     * 게시글 기능
     */

    @Transactional
    public Long addPost(PrivatePostDto privatePostDto) {

        Optional<Member> opMember = memberRepository.findById(privatePostDto.getMemberId());

        if(opMember.isEmpty()) {
            throw new RuntimeException("Wrong user Id!");
        }

        PrivatePost privatePost = PrivatePost.builder()
                .content(privatePostDto.getContent())
                .title(privatePostDto.getTitle())
                .photoUrl(privatePostDto.getPhotoUrl())
                .member(opMember.get())
                .schoolName(privatePostDto.getSchoolName())
                .communityType(privatePostDto.getCommunityType())
                .build();

        privatePostRepository.save(privatePost);
        return  privatePost.getId();
    }

    @Transactional
    public List<PrivatePostInfoDto> getPostBySchoolAndCommunity(String communityName, PrivateCommunityType communityType, int pageSize, int pageNumber) {
        Pageable pageable = (Pageable) PageRequest.of(pageNumber, pageSize);
        return privatePostRepository.getPostByNameAndType(communityName, communityType, pageable);
    }

    @Transactional
    public List<PrivatePostInfoDto> getPostByMember(Long memberId, int pageSize, int pageNumber) {
        Pageable pageable = (Pageable) PageRequest.of(pageNumber, pageSize);
        return privatePostRepository.getPostByMember(memberId, pageable);
    }

    @Transactional
    public List<PrivatePostInfoDto> getPostByCommunityAndMember(PrivateCommunityType communityType, Long memberId, int pageSize, int pageNumber) {
        Pageable pageable = (Pageable) PageRequest.of(pageNumber, pageSize);
        return privatePostRepository.getPostByCommunityAndMember(communityType, memberId, pageable);
    }

    /*
     * 게시글 좋아요 기능
     */

    @Transactional
    public Long addPostLike(Long postId, Long memberId){
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<PrivatePost> optionalPrivatePost = privatePostRepository.findById(postId);

        if(optionalMember.isEmpty() || optionalPrivatePost.isEmpty()) {
            throw new RuntimeException("invalid post or member Id!!");
        }
        else {
            if(privatePostLikeRepository.existsByMemberAndPost(optionalMember.get(), optionalPrivatePost.get())) {
                throw new RuntimeException("already existing like!!");
            }
            PrivatePostLike privatePostLike = PrivatePostLike.builder()
                    .member(optionalMember.get())
                    .post(optionalPrivatePost.get())
                    .build();
            privatePostLikeRepository.save(privatePostLike);
            return privatePostLike.getId();
        }
    }

    @Transactional
    public Long deletePostLike(Long postId, Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<PrivatePost> optionalPrivatePost = privatePostRepository.findById(postId);

        if(optionalMember.isEmpty() || optionalPrivatePost.isEmpty()) {
            throw new RuntimeException("invalid post or member Id!!");
        }
        else {

            if(privatePostLikeRepository.existsByMemberAndPost(optionalMember.get(), optionalPrivatePost.get())) {
                privatePostLikeRepository.deleteByMemberAndPost(optionalMember.get(), optionalPrivatePost.get());
                return postId;
            }
            else {
                throw new RuntimeException("like doesn't exist!!");
            }

        }
    }

    @Transactional
    public Boolean isUserLikesPost(Long postId, Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<PrivatePost> optionalPrivatePost = privatePostRepository.findById(postId);

        if(optionalMember.isEmpty() || optionalPrivatePost.isEmpty()) {
            throw new RuntimeException("invalid post or member Id!!");
        }
        else {
            return privatePostLikeRepository.existsByMemberAndPost(optionalMember.get(), optionalPrivatePost.get());
        }

    }

    @Transactional
    public int likeCountOfPost(Long postId) {
        Optional<PrivatePost> optionalPrivatePost = privatePostRepository.findById(postId);

        if(optionalPrivatePost.isEmpty()) {
            throw new RuntimeException("non-existent post!!");
        }
        else {
            return privatePostLikeRepository.likeCountOfPost(postId);
        }

    }

    /*
     * 댓글 기능
     */

    @Transactional
    public Long addComment(PrivateCommentDto privateCommentDto) {
        Long memberId = privateCommentDto.getMemberId();
        Long postId = privateCommentDto.getPostId();

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<PrivatePost> optionalPrivatePost = privatePostRepository.findById(postId);

        if(optionalMember.isEmpty() || optionalPrivatePost.isEmpty()) {
            throw new RuntimeException("invalid user or post id");
        }
        else{
            PrivateComment privateComment;
            if(privateCommentDto.getParentCommentId() == null) {
                privateComment = PrivateComment.builder()
                        .post(optionalPrivatePost.get())
                        .member(optionalMember.get())
                        .content(privateCommentDto.getContent())
                        .isAnonymous(privateCommentDto.isAnonymous())
                        .parentComment(null)
                        .build();
            }
            else {
                Optional<PrivateComment> commentOptional = privateCommentRepository.findById(privateCommentDto.getParentCommentId());

                if(commentOptional.isEmpty()) {
                    throw new RuntimeException("invalid postId!!");
                }
                privateComment = PrivateComment.builder()
                        .post(optionalPrivatePost.get())
                        .member(optionalMember.get())
                        .content(privateCommentDto.getContent())
                        .isAnonymous(privateCommentDto.isAnonymous())
                        .parentComment(commentOptional.get())
                        .build();
            }
            privateCommentRepository.save(privateComment);
            return privateComment.getId();
        }

    }

    @Transactional
    public List<PrivateCommentInfoDto> getCommentByPost(Long postId) {

        List<PrivateComment> commentNoReplies = privateCommentRepository.findByParentComment(null);

        List<PrivateCommentInfoDto> ans = commentNoReplies.stream().map(s -> {
            return PrivateCommentInfoDto.builder()
                    .id(s.getId())
                    .memberId(s.getMember().getId())
                    .memberName(s.getMember().getUsername())
                    .content(s.getContent())
                    .createdAt(s.getCreatedAt())
                    .editedAt(s.getEditedAt())
                    .isDeleted(s.isDeleted())
                    .isAnonymous(s.isAnonymous())
                    .reply(s.getReply().stream().map(t -> {
                        return PrivateReplyDto.builder()
                                .id(t.getId())
                                .memberId(t.getMember().getId())
                                .memberName(t.getMember().getUsername())
                                .content(t.getContent())
                                .createdAt(t.getCreatedAt())
                                .editedAt(t.getEditedAt())
                                .isDeleted(t.isDeleted())
                                .isAnonymous(t.isAnonymous())
                                .build();
                    }).collect(Collectors.toList()))
                    .build();
        }).collect(Collectors.toList());

        return ans;

    }


//    @Transactional
//    public List<PrivateComment> getMyComment(Long memberId) {
//        Optional<Member> optionalMember = memberRepository.findById(memberId);
//
//        if(optionalMember.isEmpty()) {
//            throw new RuntimeException("non-existent user!!");
//        }
//        else {
//            return privateCommentRepository.findByMember(optionalMember.get());
//        }
//    }


}

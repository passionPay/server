package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostDto;
import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostInfoDto;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateCommunityType;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePost;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePostLike;
import com.passionPay.passionPayBackEnd.repository.MemberRepository;
import com.passionPay.passionPayBackEnd.repository.PrivatePostLikeRepository;
import com.passionPay.passionPayBackEnd.repository.PrivatePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrivateService {

    private final MemberRepository memberRepository;
    private final PrivatePostRepository privatePostRepository;
    private final PrivatePostLikeRepository privatePostLikeRepository;

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




}

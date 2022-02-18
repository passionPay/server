package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostDto;
import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostInfoDto;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateCommunityType;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePost;
import com.passionPay.passionPayBackEnd.repository.MemberRepository;
import com.passionPay.passionPayBackEnd.repository.PrivatePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrivateService {

    private final MemberRepository memberRepository;
    private final PrivatePostRepository privatePostRepository;

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

}

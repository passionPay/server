package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.FollowInfoDto;
import com.passionPay.passionPayBackEnd.domain.Follow;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.repository.FollowRepository;
import com.passionPay.passionPayBackEnd.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public FollowInfoDto addFollow(Long user, Long follower) {

        if(Objects.equals(user, follower)) throw new RuntimeException("can't follow yourself");

        Optional<Member> op_member_user = memberRepository.findById(user);
        Optional<Member> op_member_following = memberRepository.findById(follower);

        if(op_member_user.isPresent() && op_member_following.isPresent()) {

            if(followRepository.existsByUserAndFollowing(op_member_user.get(), op_member_following.get())) {
                throw new RuntimeException("existing user, following mapping");
            }
            else {

                // 팔로우 상대가 비공개 계정
                Follow follow;
                if(op_member_following.get().isPersonal()) {
                    follow = Follow.builder()
                            .user(op_member_user.get())
                            .following(op_member_following.get())
                            .valid(false)
                            .build();
                }
                // 팔로우 상대가 공개 계정
                else {
                    follow = Follow.builder()
                            .user(op_member_user.get())
                            .following(op_member_following.get())
                            .valid(true)
                            .build();
                }

                followRepository.save(follow);
                return FollowInfoDto.of(follow);
            }

        }
        else {
            throw new RuntimeException("invalid userId");
        }
    }

    @Transactional
    public Long deleteFollow(Long followId) {

        if(followRepository.existsById(followId)) {
            Follow follow = followRepository.findById(followId).get();
            follow.setUser(null);
            follow.setFollowing(null);
            followRepository.deleteById(followId);
            return followId;
        }
        else {
            throw new RuntimeException("Invalid followId");
        }

    }

    @Transactional
    public List<FollowInfoDto> getFollowing(Long memberId) {

        Optional<Member> opMember = memberRepository.findById(memberId);

        if(opMember.isPresent()) {
            return followRepository.getFollowingOBByMember(opMember.get());
        }
        else {
            throw new RuntimeException("Invalid followId");
        }

    }

    @Transactional
    public List<FollowInfoDto> getFollower(Long memberId) {

        Optional<Member> opMember = memberRepository.findById(memberId);

        if(opMember.isPresent()) {
            return followRepository.getFollowingIBByMember(opMember.get());
        }
        else {
            throw new RuntimeException("Invalid followId");
        }

    }

    @Transactional
    public List<FollowInfoDto> getFollowingOBRequest(Long memberId) {
        if(memberRepository.existsById(memberId)) {
            return followRepository.getFollowingOBRequest(memberRepository.getById(memberId));
        }
        else {
            throw new RuntimeException("Invalid followId");
        }
    }

    @Transactional
    public List<FollowInfoDto> getFollowingIBRequest(Long memberId) {
        if(memberRepository.existsById(memberId)) {
            return followRepository.getFollowingIBRequest(memberRepository.getById(memberId));
        }
        else {
            throw new RuntimeException("Invalid followId");
        }
    }

    @Transactional
    public Boolean acceptFollowRequest(Long followId) {
        if(followRepository.existsById(followId)) {
            followRepository.acceptFollowRequest(followId);
            return true;
        }
        else {
            throw new RuntimeException("Invalid followId");
        }
    }
}

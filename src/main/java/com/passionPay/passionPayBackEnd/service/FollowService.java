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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public FollowInfoDto addFollow(Long user, Long follower) {

        Optional<Member> op_member_user = memberRepository.findById(user);
        Optional<Member> op_member_follower = memberRepository.findById(follower);

        if(op_member_user.isPresent() && op_member_follower.isPresent()) {

            if(followRepository.existsByUserAndFollower(op_member_user.get(), op_member_follower.get())) {
                throw new RuntimeException("existing user, follower mapping");
            }
            else {
                Follow follow = Follow.builder()
                        .user(op_member_user.get())
                        .follower(op_member_follower.get())
                        .build();
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
//            System.out.println("**************** " + followRepository.existsById(followId));
//            System.out.println("**************** " + followRepository.findById(followId).get().getId());
            Follow follow = followRepository.findById(followId).get();
            follow.setUser(null);
            follow.setFollower(null);
            followRepository.deleteById(followId);
//            followRepository.delete
            return followId;
        }
        else {
            throw new RuntimeException("Invalid followId");
//            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

    }

    @Transactional
    public Optional<List<FollowInfoDto> > getFollowing(Long memberId) {

        Optional<Member> opMember = memberRepository.findById(memberId);

        if(opMember.isPresent()) {
            Optional< List<FollowInfoDto> > opFollowingList = followRepository.getFollowingByUser(opMember.get());
            return opFollowingList;
        }
        else {
            throw new RuntimeException("Invalid followId");
        }

    }

    @Transactional
    public Optional<List<FollowInfoDto> > getFollower(Long memberId) {

        Optional<Member> opMember = memberRepository.findById(memberId);

        if(opMember.isPresent()) {
            Optional< List<FollowInfoDto> > opFollowingList = followRepository.getFollowerByUser(opMember.get());
            return opFollowingList;
        }
        else {
            throw new RuntimeException("Invalid followId");
        }

    }



}

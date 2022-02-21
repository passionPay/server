package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import com.passionPay.passionPayBackEnd.controller.dto.PlannerDto.PlannerLikeDto;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Planner;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.PlannerLike;
import com.passionPay.passionPayBackEnd.repository.MemberRepository;
import com.passionPay.passionPayBackEnd.repository.PlannerLikeRepository;
import com.passionPay.passionPayBackEnd.repository.PlannerRepository;
import com.passionPay.passionPayBackEnd.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlannerLikeService {

    private final PlannerLikeRepository plannerLikeRepository;
    private final PlannerRepository plannerRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public PlannerLikeService(PlannerLikeRepository plannerLikeRepository, PlannerRepository plannerRepository, MemberRepository memberRepository) {
        this.plannerLikeRepository = plannerLikeRepository;
        this.plannerRepository = plannerRepository;
        this.memberRepository = memberRepository;
    }

    // 저장
    @Transactional
    public PlannerLikeDto savePlannerLike(Planner planner, Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        // 유효하지 않은 memberId
        if (member == null) throw new RuntimeException("유효하지 않은 멤버");
        // 본인 플래너에 좋아요 X
        // TODO : memberId를 매개변수로 가져오지 않고 Security Util 에서 추출하면 되지 않나?
        if (memberId == planner.getMemberId()) throw new RuntimeException("잘못된 접근");
        PlannerLike plannerLike = PlannerLike.builder()
                .planner(planner)
                .member(member)
                .build();
        plannerLikeRepository.save(plannerLike);
        return PlannerLikeDto.builder()
                .likeId(plannerLike.getId())
                .plannerId(plannerLike.getPlanner().getPlannerId())
                .memberId(plannerLike.getMember().getId())
                .build();
    }

    // 조회
    @Transactional
    public PlannerLike getPlannerLikeById(Long fireId) {
        return plannerLikeRepository.findById(fireId).orElse(null);
    }

    public List<PlannerLike> getPlannerLikeByMemberId(Long memberId) {
        return plannerLikeRepository.findByMemberId(memberId);
    }

    @Transactional
    public Long getPlannerLikeCount(Long plannerId) {
        return plannerRepository.findByPlannerId(plannerId)
                .map(plannerLikeRepository::findCountByPlannerId)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public List<MemberInfoDto> getPlannerLikeMember(Long plannerId) {
        return plannerRepository.findByPlannerId(plannerId)
                .map(plannerLikeRepository::findMemberByPlannerId)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public List<PlannerLike> getPlannerLikeByPlannerId(Long plannerId) {
        Planner planner = plannerRepository.getById(plannerId);
        return plannerLikeRepository.findByPlanner(planner);
    }

    // 삭제
    @Transactional
    public boolean deletePlannerLike(PlannerLike plannerLike) {
        plannerLikeRepository.delete(plannerLike);
        return true;
    }

    @Transactional
    public boolean deletePlannerLikeById(Long fireId) {
        plannerLikeRepository.deleteById(fireId);
        return true;
    }

    @Transactional
    public boolean deletePlannerLikeByMemberIdAndPlanner(Long memberId, Planner planner) {
        plannerLikeRepository.deleteByMemberIdAndPlanner(memberId, planner);
        return true;
    }
}

package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import com.passionPay.passionPayBackEnd.controller.dto.PlannerDto.PlannerLikeDto;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Planner;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.PlannerLike;
import com.passionPay.passionPayBackEnd.repository.MemberRepository;
import com.passionPay.passionPayBackEnd.service.AuthService;
import com.passionPay.passionPayBackEnd.service.PlannerLikeService;
import com.passionPay.passionPayBackEnd.service.PlannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planner")
public class PlannerLikeController {

    private final PlannerLikeService plannerLikeService;
    private final PlannerService plannerService;

    public PlannerLikeController(PlannerLikeService plannerLikeService, PlannerService plannerService) {
        this.plannerLikeService = plannerLikeService;
        this.plannerService = plannerService;
    }

    // 플래너 좋아요 개수 반환
    @GetMapping("/{plannerId}/like/count")
    public ResponseEntity<Long> getPlannerLikeCount(
            @PathVariable(name="plannerId") Long plannerId) {
        return ResponseEntity.ok(plannerLikeService.getPlannerLikeCount(plannerId));
    }

    // 플래너에 좋아요한 사람들
    @GetMapping("/{plannerId}/like/members")
    public ResponseEntity<List<MemberInfoDto>> getPlannerLikeMember(
            @PathVariable(name="plannerId") Long plannerId) {
        return ResponseEntity.ok(plannerLikeService.getPlannerLikeMember(plannerId));
    }

    @PostMapping("/{plannerId}/like/{memberId}")
    public ResponseEntity<PlannerLikeDto> savePlannerLike(
            @PathVariable(name="plannerId") Long plannerId, @PathVariable(name="memberId") Long memberId) {
        Planner planner = plannerService.getPlannerById(plannerId);
        return ResponseEntity.ok(plannerLikeService.savePlannerLike(planner, memberId));
    }

    @DeleteMapping("/{plannerId}/like/{memberId}")
    public ResponseEntity<Boolean> deleteFire(
            @PathVariable(name="plannerId") Long plannerId, @PathVariable(name="memberId") Long memberId) {
        Planner planner = plannerService.getPlannerById(plannerId);
        return ResponseEntity.ok(plannerLikeService.deletePlannerLikeByMemberIdAndPlanner(memberId, planner));
    }
}

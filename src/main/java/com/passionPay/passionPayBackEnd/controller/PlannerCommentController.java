package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.PlannerDto.PlannerCommentDto;
import com.passionPay.passionPayBackEnd.service.PlannerCommentService;
import com.passionPay.passionPayBackEnd.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planner")
public class PlannerCommentController {

    private final PlannerCommentService plannerCommentService;

    public PlannerCommentController(PlannerCommentService plannerCommentService) {
        this.plannerCommentService = plannerCommentService;
    }

    // save
    @PostMapping("/{plannerId}/comment")
    public ResponseEntity<PlannerCommentDto> save(@PathVariable("plannerId") Long plannerId, String comment) {
        return ResponseEntity.ok(plannerCommentService.save(plannerId, comment, SecurityUtil.getCurrentMemberId()));
    }

    // get
    @GetMapping("/{plannerId}/comment")
    public ResponseEntity<List<PlannerCommentDto>> getAllComment(@PathVariable("plannerId") Long plannerId) {
        return ResponseEntity.ok(plannerCommentService.getByPlannerId(plannerId));
    }

    // delete
    @DeleteMapping("/comment/{plannerCommentId}")
    public ResponseEntity<Boolean> deletePlannerComment(@PathVariable("plannerCommentId") Long plannerCommentId) {
        return ResponseEntity.ok(plannerCommentService.deleteByPlannerCommentId(plannerCommentId));
    }
}

package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.ProfileDto.ProfileDto;
import com.passionPay.passionPayBackEnd.service.PlannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final PlannerService plannerService;

    public ProfileController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    // 프로필 메인 = 일주일 통계 + 오늘 목표
    @GetMapping("/main/{date}")
    public ResponseEntity<ProfileDto> getProfileMain(@PathVariable(name="date") String date) {
        return ResponseEntity.ok(plannerService.getProfileMain(date));
    }

    // 프로필 일간 통계
    @GetMapping("/statistics/day/{date}")
    public ResponseEntity<ProfileDto> getDayProfile(@PathVariable(name="date") String date) {
        return ResponseEntity.ok(plannerService.getTodayProfileStatistics(date));
    }

    // 프로필 주간 통계
    @GetMapping("/statistics/week/{date}")
    public ResponseEntity<List<Integer>> getWeekProfile(@PathVariable(name="date") String date) {
        return ResponseEntity.ok(plannerService.getWeekProfile(date));
    }

    // 프로필 월간 통계
    @GetMapping("/statistics/month/{date}")
    public ResponseEntity<List<Integer>> getMonthProfile(@PathVariable(name="date") String date) {
        return ResponseEntity.ok(plannerService.getMonthProfile(date));
    }
}

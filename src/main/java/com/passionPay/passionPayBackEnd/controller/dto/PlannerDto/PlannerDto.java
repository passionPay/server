package com.passionPay.passionPayBackEnd.controller.dto.PlannerDto;

import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Planner;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Timestamp;
import com.passionPay.passionPayBackEnd.util.DateUtil;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannerDto {
    private Long plannerId;
    private String date; // YYYY-MM-DD
    private long totalTime;
    private long targetTime;
    private long timeRate; // 시간 달성률
    private long taskRate; // task 달성률
    private int fireCount; // 받은 열정 개수
    private String comment; // 각오
    private long dDay; // d-day 까지 남은 날짜 수
    private String evaluation; // 오늘 하루 평가
    private List<SubjectTaskDto> tasks; // 플래너와 연관된 task
    private List<Timestamp> timestamps; // 플래너와 연관된 timestamp

    public static PlannerDtoBuilder from(Planner planner) {
        if (planner == null) return null;

        return PlannerDto.builder()
                .plannerId(planner.getPlannerId())
                .totalTime(planner.getCurrentStudyTime().toSecondOfDay() * 1000)
                .targetTime(planner.getExpectedStudyTime().toSecondOfDay() * 1000)
                .date(DateUtil.formatDateToString(planner.getDate()))
                .comment(planner.getComment())
                .dDay(DateUtil.formatDDayToInt(planner.getDDay()))
                .fireCount(planner.getLikeCount())
                .evaluation(planner.getEvaluation());
    }
}

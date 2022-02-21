package com.passionPay.passionPayBackEnd.controller.dto.PlannerDto;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannerRequestDto {
    private String date;
    private String expectedStudyTime;
    private String currentStudyTime;
    private String comment;
    private String evaluation;
    private String dDay;
}

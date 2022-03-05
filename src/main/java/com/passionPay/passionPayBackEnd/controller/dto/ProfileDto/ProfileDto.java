package com.passionPay.passionPayBackEnd.controller.dto.ProfileDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    // array of study time for seven days starting from input date
    private List<Integer> studyTime;
    private int dayTimeGoal;
    private int todayStudyTime;
}

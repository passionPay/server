package com.passionPay.passionPayBackEnd.controller.dto.GroupDto;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProgressDto {
    private int timeProgress;
    private int missionProgress;
}

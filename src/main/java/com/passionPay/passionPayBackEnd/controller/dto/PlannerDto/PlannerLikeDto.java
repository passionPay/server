package com.passionPay.passionPayBackEnd.controller.dto.PlannerDto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannerLikeDto {
    private Long likeId;
    private Long memberId;
    private Long plannerId;
}

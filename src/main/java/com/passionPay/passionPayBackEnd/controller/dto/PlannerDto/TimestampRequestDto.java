package com.passionPay.passionPayBackEnd.controller.dto.PlannerDto;

import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Task;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Timestamp;
import com.passionPay.passionPayBackEnd.util.DateUtil;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimestampRequestDto {
    private String startTime;
    private String endTime;

    static public Timestamp from(TimestampRequestDto timestampRequestDto, Task task) {
        return Timestamp.builder()
                .task(task)
                .startTime(DateUtil.parseStringToTime(timestampRequestDto.getStartTime()))
                .endTime(DateUtil.parseStringToTime(timestampRequestDto.getEndTime()))
                .build();
    }
}

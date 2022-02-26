package com.passionPay.passionPayBackEnd.controller.dto.PlannerDto;

import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Task;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long taskId;
    private String taskName;
    private int status;
    private long totalTime; // ms
    private String color;

    public static TaskDto from(Task task) {
        if (task == null) return null;

        return TaskDto.builder()
                .taskId(task.getTaskId())
                .totalTime(task.getTotalTime())
                .taskName(task.getTaskName())
                .status(task.getStatus())
                .build();
    }
}

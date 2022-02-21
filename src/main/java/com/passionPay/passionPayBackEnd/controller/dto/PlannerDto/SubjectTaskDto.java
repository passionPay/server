package com.passionPay.passionPayBackEnd.controller.dto.PlannerDto;

import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Task;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectTaskDto {
    private Long subjectId;
    private String subjectTitle;
    private List<TaskDto> tasks = new ArrayList<>();

    public boolean addTask(TaskDto taskDto) {
        if (taskDto == null) return false;
        return this.tasks.add(taskDto);
    }

    static public SubjectTaskDto from(Task task) {
        List<TaskDto> tasks = new ArrayList<>();
        tasks.add(TaskDto.from(task));

        return SubjectTaskDto.builder()
                .subjectId(task.getSubject().getSubjectId())
                .subjectTitle(task.getSubject().getTitle())
                .tasks(tasks)
                .build();
    }
}

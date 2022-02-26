package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.PlannerDto.TimestampRequestDto;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Task;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Timestamp;
import com.passionPay.passionPayBackEnd.service.TaskService;
import com.passionPay.passionPayBackEnd.service.TimestampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planner")
public class TimestampController {

    private final TimestampService timestampService;
    private final TaskService taskService;

    @Autowired
    public TimestampController(TimestampService timestampService, TaskService taskService) {
        this.timestampService = timestampService;
        this.taskService = taskService;
    }

    // 태스크와 연관된 timestamp 생성
    @PostMapping("/task/{taskId}/timestamp")
    public ResponseEntity<Timestamp> saveTimestamp(
            @PathVariable(name="taskId") Long taskId, @RequestBody TimestampRequestDto timestampRequestDto) {
        Task task = taskService.getTaskById(taskId);
        if (task == null) throw new RuntimeException("유효하지 않은 task");
        return ResponseEntity.ok(timestampService
                .saveTimestamp(timestampRequestDto, task));
    }

    // task 와 연관된 timestamp color 전부 수정
    /*
    @PutMapping("/task/{taskId}/timestamp/color")
    public ResponseEntity<Integer> updateTimestampColor(
            @PathVariable(name="taskId") Long taskId, @RequestBody String timestampColor) {
        return ResponseEntity.ok(timestampService.updateTimestampColor(taskId, timestampColor));
    }
    */

    /*
    // timestamp 종료시간 설정
    @PutMapping("/task/timestamp/{timestampId}/end-time")
    public ResponseEntity<Timestamp> updateTimestampEndTime(
            @PathVariable(name="timestampId") Long timestampId, @RequestBody String endTime) {
        return ResponseEntity.ok(timestampService.updateTimestampEndTime(timestampId, endTime));
    }
    */

    @DeleteMapping("/task/timestamp/{timestampId}")
    public ResponseEntity<Boolean> deleteTimestamp(
            @PathVariable(name="timestampId") Long timestampId) {
        return ResponseEntity.ok(timestampService.deleteTimestampById(timestampId));
    }
}

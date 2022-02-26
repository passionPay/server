package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.PlannerDto.TimestampRequestDto;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Task;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Timestamp;
import com.passionPay.passionPayBackEnd.repository.TaskRepository;
import com.passionPay.passionPayBackEnd.repository.TimestampRepository;
import com.passionPay.passionPayBackEnd.util.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TimestampService {

    final private TimestampRepository timestampRepository;
    final private TaskRepository taskRepository;

    public TimestampService(TimestampRepository timestampRepository, TaskRepository taskRepository) {
        this.timestampRepository = timestampRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Timestamp saveTimestamp(TimestampRequestDto timestampRequestDto, Task task) {
        Timestamp timestamp = TimestampRequestDto.from(timestampRequestDto, task);

        // endTime 이 startTime 이후인지 검사
        if (timestamp.getEndTime().isBefore(timestamp.getStartTime()))
            throw new RuntimeException("잘못된 형식의 종료시간");

        timestampRepository.save(timestamp);
        // task 에 연관관계 추가
        task.addTimestamp(timestamp);
        // update task _ total time
        task.setTotalTime(task.getTotalTime()
                + DateUtil.getTimeBetween(timestamp.getStartTime(), timestamp.getEndTime()));
        taskRepository.save(task);
        return timestamp;
    }

    @Transactional
    public List<Timestamp> getTimestampByTask(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findByTaskId(taskId);
       return taskOptional
                .map(timestampRepository::findByTask)
                .orElseThrow(RuntimeException::new);
    }

    /*
    @Transactional
    public int updateTimestampColor(Long taskId, String timestampColor) {
        Optional<Task> taskOptional = taskRepository.findByTaskId(taskId);

        if (timestampColor.length() != 7 || !timestampColor.startsWith("#"))
            throw new RuntimeException("유효하지 않은 timestamp color!");

        return taskOptional
                .map(task -> timestampRepository.updateTimestampColorByTask(task, timestampColor))
                .orElseThrow(RuntimeException::new);
    }
    */

    @Transactional
    public Timestamp updateTimestampEndTime(Long timestampId, String endTime) {
        Optional<Timestamp> timestampOptional = timestampRepository.findById(timestampId);
        return timestampOptional
                .map(timestamp -> {
                    LocalTime endLocalTime = DateUtil.parseStringToTime(endTime);
                    // endTime 이 startTime 이후인지 검사
                    if (endLocalTime.isBefore(timestamp.getStartTime()))
                        throw new RuntimeException("잘못된 형식의 종료시간");
                    timestamp.setEndTime(endLocalTime);

                    // update task total time
                    Task task = timestamp.getTask();
                    task.setTotalTime(DateUtil.getTimeBetween(timestamp.getStartTime(), endLocalTime));
                    taskRepository.save(task);
                    return timestampRepository.save(timestamp);
                }).orElseThrow(RuntimeException::new);
    }

    @Transactional
    public boolean deleteTimestamp(Timestamp timestamp) {
        if (timestamp == null) return false;
        timestampRepository.delete(timestamp);
        return true;
    }

    @Transactional
    public boolean deleteTimestampById(Long timestampId) {
        timestampRepository.deleteById(timestampId);
        return true;
    }
}

package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.PlannerDto.TaskDto;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Subject;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Task;
import com.passionPay.passionPayBackEnd.repository.PlannerRepository;
import com.passionPay.passionPayBackEnd.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final PlannerRepository plannerRepository;

    public TaskService(TaskRepository taskRepository, PlannerRepository plannerRepository) {
        this.taskRepository = taskRepository;
        this.plannerRepository = plannerRepository;
    }

    // 새로운 태스크 생성
    @Transactional
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    // 태스크 조회
    @Transactional
    public Task getTaskById(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findByTaskId(taskId);
        return taskOptional.orElse(null);
    }

    @Transactional
    public List<Task> getTaskBySubject(Subject subject) {
        return taskRepository.findBySubject(subject);
    }

    @Transactional
    public List<TaskDto> getTaskByPlanner(Long plannerId) {
        return plannerRepository.findByPlannerId(plannerId)
                .map(taskRepository::findByPlanner)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public boolean deleteTaskById(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findByTaskId(taskId);
        return taskOptional.map(task-> {
            // TODO: task 접근 권한 확인하기
            taskRepository.delete(task);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean deleteTask(Task task) {
        taskRepository.delete(task);
        return true;
    }
}

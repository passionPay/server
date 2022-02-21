package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.PlannerDto.PlannerDto;
import com.passionPay.passionPayBackEnd.controller.dto.PlannerDto.PlannerRequestDto;
import com.passionPay.passionPayBackEnd.controller.dto.PlannerDto.SubjectTaskDto;
import com.passionPay.passionPayBackEnd.controller.dto.PlannerDto.TaskDto;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Planner;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Task;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Timestamp;
import com.passionPay.passionPayBackEnd.repository.PlannerRepository;
import com.passionPay.passionPayBackEnd.repository.TimestampRepository;
import com.passionPay.passionPayBackEnd.util.DateUtil;
import com.passionPay.passionPayBackEnd.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PlannerService {

    private final PlannerRepository plannerRepository;
    private final TimestampRepository timestampRepository;

    @Autowired
    public PlannerService(PlannerRepository plannerRepository, TimestampRepository timestampRepository) {
        this.plannerRepository = plannerRepository;
        this.timestampRepository = timestampRepository;
    }

    // 새로운 플래너 생성
    @Transactional
    public Planner savePlanner(Planner planner) {
        return plannerRepository.save(planner);
    }

    @Transactional
    public Planner addTaskToPlanner(Planner planner, Task task) {
        planner.addTask(task);
        return plannerRepository.save(planner);
    }

    // 조회
    @Transactional
    public Planner getPlannerById(Long plannerId) {
        return plannerRepository.findByPlannerId(plannerId).orElse(null);
    }

    @Transactional
    public List<Planner> getPlannerByMemberId(Long memberId) {
        return plannerRepository.findByMemberId(memberId);
    }

    @Transactional
    public PlannerDto getPlannerByMemberIdAndDate(Long memberId, LocalDate date) {
        Optional<Planner> plannerOptional = plannerRepository.findByMemberIdAndDate(memberId, date);

        // TODO: 너무 복잡함. 수정하도록 하자.
        return plannerOptional.map(planner -> {
            AtomicInteger achievedTasks = new AtomicInteger();

            List<SubjectTaskDto> subjectTaskDtoList = new ArrayList<>();

            long currentStudyTime = 0;

            for (Task task : planner.getTasks()) {
                if (task.getStatus() > 0) achievedTasks.addAndGet(1);

                currentStudyTime += task.getTotalTime();

                boolean flag = true;

                for (SubjectTaskDto subjectTaskDto : subjectTaskDtoList) {
                    if (subjectTaskDto.getSubjectTitle().equalsIgnoreCase(task.getSubject().getTitle())) {
                        subjectTaskDto.addTask(TaskDto.from(task));
                        flag = false;
                        break;
                    }
                }

                if (flag) subjectTaskDtoList.add(SubjectTaskDto.from(task));
            }

            int expectedStudyTime = planner.getExpectedStudyTime().toSecondOfDay() * 1000;

            // 시간 달성률
            double timeRate = (double) currentStudyTime / expectedStudyTime * 100;
            // 업무 달성률
            double taskRate = 0;

            if (planner.getTasks().size() > 0)
                taskRate = (double) achievedTasks.get() / planner.getTasks().size() * 100;

            if (timeRate > 100) timeRate = 100;

            return PlannerDto.from(planner)
                    .fireCount(planner.getLikeCount())
                    .targetTime(expectedStudyTime)
                    .totalTime(currentStudyTime)
                    .timeRate((long) Math.floor(timeRate))
                    .taskRate((long) Math.floor(taskRate))
                    .tasks(subjectTaskDtoList)
                    .timestamps(timestampRepository.findByPlannerOrderById(planner))
                    .build();
        }).orElse(null);
    }

    @Transactional
    public Planner updatePlanner(PlannerRequestDto plannerRequestDto, Planner planner) {
        // comment
        planner.setComment(plannerRequestDto.getComment());
        // d-day
        planner.setDDay(DateUtil.parseStringToDate(plannerRequestDto.getDDay()));
        // expected study time
        planner.setExpectedStudyTime(DateUtil.parseStringToTime(plannerRequestDto.getExpectedStudyTime()));
        // current study time
        planner.setCurrentStudyTime(DateUtil.parseStringToTime(plannerRequestDto.getCurrentStudyTime()));
        // evaluation
        planner.setComment(plannerRequestDto.getComment());
        return plannerRepository.save(planner);
    }

    // TODO: 플래너 삭제할 때 task 도 지워지도록 - 연관관계 제거
    @Transactional
    public boolean deletePlanner(Long plannerId) {
        Optional<Planner> plannerOptional = plannerRepository.findByPlannerId(plannerId);
        return plannerOptional.map(planner -> {
            if (SecurityUtil.getCurrentMemberId() != planner.getMemberId()) return false;
            plannerRepository.delete(planner);
            return true;
        }).orElseThrow(RuntimeException::new);
    }

    @Transactional
    public Planner deleteTaskFromPlanner(Planner planner, Task task) {
        planner.deleteTask(task);
        return plannerRepository.save(planner);
    }
}

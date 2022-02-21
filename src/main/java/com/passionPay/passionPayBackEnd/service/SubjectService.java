package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Subject;
import com.passionPay.passionPayBackEnd.repository.PlannerRepository;
import com.passionPay.passionPayBackEnd.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final PlannerRepository  plannerRepository;

    public SubjectService(SubjectRepository subjectRepository, PlannerRepository plannerRepository) {
        this.subjectRepository = subjectRepository;
        this.plannerRepository = plannerRepository;
    }

    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Subject getSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId).orElse(null);
    }

    public List<Subject> getSubjectByMemberId(Long memberId) {
        return subjectRepository.findByMemberId(memberId);
    }

    public Subject getSubjectByTitle(String title) {
        return subjectRepository.findByTitle(title).orElse(null);
    }

    public long getSubjectTotalTimeByTitle(String title, Long plannerId) {
        return plannerRepository.findByPlannerId(plannerId)
                .map(planner -> subjectRepository.findTotalTimeByTitle(title, planner))
                .orElseThrow(RuntimeException::new);
    }
}

package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Subject;
import com.passionPay.passionPayBackEnd.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planner")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/{memberId}/subject")
    public ResponseEntity<List<Subject>> getSubjectByMemberId(@PathVariable(name="memberId") Long memberId) {
        return ResponseEntity.ok(subjectService.getSubjectByMemberId(memberId));
    }

    @GetMapping("/{plannerId}/subject/{title}/total-time")
    public ResponseEntity<Long> getSubjectTotalTimeByTitle(
            @PathVariable(name="plannerId") Long plannerId, @PathVariable(name="title") String title) {
        return ResponseEntity.ok(subjectService.getSubjectTotalTimeByTitle(title, plannerId));
    }

    @PostMapping("/{memberId}/subject")
    public ResponseEntity<Subject> saveSubject(
            @PathVariable(name="memberId") Long memberId, @RequestBody String subjectTitle) {
        Subject subject = Subject.builder()
                .memberId(memberId)
                .title(subjectTitle)
                .build();
        return ResponseEntity.ok(subjectService.saveSubject(subject));
    }

    @PutMapping("/subject/{subjectId}")
    public ResponseEntity<Subject> updateSubject(
            @PathVariable(name="subjectId") Long subjectId, @RequestBody String subjectTitle
    ) {
        Subject subject = subjectService.getSubjectById(subjectId);
        if (subject == null) throw new RuntimeException("유효하지 않은 과목명");
        subject.setTitle(subjectTitle);
        return ResponseEntity.ok(subjectService.saveSubject(subject));
    }
}

package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Planner;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject save(Subject subject);
    List<Subject> findByMemberId(Long memberId);
    Optional<Subject> findByTitle(String title);

    @Query("SELECT SUM(t.totalTime) " +
            "FROM Subject s INNER JOIN s.tasks t " +
            "WHERE s.title = :title AND t.planner = :planner")
    long findTotalTimeByTitle(@Param("title") String title, @Param("planner") Planner planner);
}

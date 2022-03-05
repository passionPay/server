package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlannerRepository extends JpaRepository<Planner, Long> {
    // 플래너 저장
    Planner save(Planner planner);

    Optional<Planner> findByPlannerId(Long aLong);
    // 플래너 전체 조회
    List<Planner> findByMemberId(Long memberId);
    // 특정 날짜 플래너만 조회
    Optional<Planner> findByMemberIdAndDate(Long memberId, LocalDate date);
    // 날짜 통계
    List<Planner> findByMemberIdAndDateBetween(Long memberId, LocalDate start, LocalDate end);

    // 플래너 삭제
    void delete(Planner planner);
}

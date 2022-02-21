package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Planner;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.PlannerLike;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlannerLikeRepository extends JpaRepository<PlannerLike, Long> {
    // like 저장
    PlannerLike save(PlannerLike like);

    // like 조회
    Optional<PlannerLike> findById(Long plannerLikeId);
    List<PlannerLike> findByMemberId(Long memberId);
    List<PlannerLike> findByPlanner(Planner planner);
        // 플래너가 받은 좋아요 개수 count
    @Query("SELECT COUNT(p.id) " +
            "FROM PlannerLike p " +
            "WHERE p.planner = :planner")
    Long findCountByPlannerId(@Param("planner") Planner planner);

    // 플래너에 좋아요 누른 사람들 조회
    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto(m.username, m.email, m.displayName, " +
            "m.activated, m.photoUrl, m.categoryName, m.stage, m.grade) " +
            "FROM PlannerLike p INNER JOIN p.member m " +
            "WHERE p.planner = :planner")
    List<MemberInfoDto> findMemberByPlannerId(@Param("planner") Planner planner);

    // like 삭제
    void delete(PlannerLike plannerLike);
    void deleteById(Long fireId);
    void deleteByMemberIdAndPlanner(Long memberId, Planner planner);
}

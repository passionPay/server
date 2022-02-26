package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMission;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupProgressRepository extends JpaRepository<GroupProgress, Long> {
    // save
    GroupProgress save(GroupProgress groupProgress);

    // find
    // 내 미션 달성률
    @Query("SELECT COUNT(gp.id) " +
            "FROM GroupProgress gp INNER JOIN gp.groupMission gm " +
            "WHERE gp.groupMemberId = :groupMemberId AND " +
            "gm = :groupMission AND gp.complete = true")
    int findCountByGroupMemberAndGroupMission(@Param("groupMemberId") Long groupMemberId, @Param("groupMission") GroupMission groupMission);
    // 그룹 평균 미션 달성률
    @Query("SELECT COUNT(gp.id) " +
            "FROM GroupProgress gp INNER JOIN gp.groupMission gm " +
            "WHERE gp.complete = true AND gm = :groupMission")
    int findCountByGroupMission(@Param("groupMission") GroupMission groupMission);

    // update

    // delete
    void delete(GroupProgress groupProgress);
    void deleteById(Long groupProgressId);
}

package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.Group;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMissionRepository extends JpaRepository<GroupMission, Long> {
    // save
    GroupMission save(GroupMission groupGoal);

    // find
    Optional<GroupMission> findById(Long aLong);
    List<GroupMission> findByGroup(Group group);

    // update

    // delete
    void delete(GroupMission groupMission);
    void deleteById(Long groupGoalId);
    void deleteByGroup(Group group);
}

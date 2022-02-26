package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.Group;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMember;
import com.passionPay.passionPayBackEnd.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    // save
    GroupMember save(GroupMember groupMember);

    // find
    Optional<GroupMember> findById(Long groupMemberId);
    Optional<GroupMember> findByMemberIdAndGroupId(Long memberId, Long groupId);
    List<GroupMember> findByMember(Member member);
    List<GroupMember> findByGroup(Group group);

    // delete
    void delete(GroupMember groupMember);
}

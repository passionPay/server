package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.Group;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMember;
import com.passionPay.passionPayBackEnd.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    // save
    GroupMember save(GroupMember groupMember);

    // find
    Optional<GroupMember> findById(Long groupMemberId);
    List<GroupMember> findByMember(Member member);
    List<GroupMember> findByGroup(Group group);

    // delete
    void delete(GroupMember groupMember);
}

package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    // save
    Group save(Group group);

    // find
    Optional<Group> findById(Long groupId);
    List<Group> findAllOrderByGroupId();

    // delete
    void delete(Group group);
    void deleteById(Long groupId);
}

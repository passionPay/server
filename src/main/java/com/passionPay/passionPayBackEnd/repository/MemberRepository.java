package com.passionPay.passionPayBackEnd.repository;


import com.passionPay.passionPayBackEnd.domain.Member;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    boolean existsByUsername(String username);

    @Modifying
    @Query("UPDATE Member m SET m.reportCount = ?2 WHERE m.id = ?1")
    void modifyReportCount(Long memberId, Integer reportCount);
}
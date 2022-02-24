package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePost;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePostReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PrivatePostReportRepository extends JpaRepository<PrivatePostReport, Long> {

    boolean existsByPostAndMember(PrivatePost post, Member member);

    @Modifying
    @Query("DELETE FROM PrivatePostReport p WHERE p.post.id = ?1")
    void deleteByPostId(Long postId);

}

package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicPost;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicPostReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PublicPostReportRepository extends JpaRepository<PublicPostReport, Long> {

    boolean existsByPostAndMember(PublicPost post, Member member);

    @Modifying
    @Query("DELETE FROM PublicPostReport p WHERE p.post.id = ?1")
    void deleteByPostId(Long postId);

}

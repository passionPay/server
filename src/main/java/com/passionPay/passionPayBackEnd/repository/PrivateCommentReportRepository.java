package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateComment;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateCommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateCommentReportRepository extends JpaRepository<PrivateCommentReport, Long> {

    boolean existsByCommentAndMember(PrivateComment comment, Member member);

    @Modifying
    @Query("DELETE FROM PrivateCommentReport p WHERE p.comment.id IN " +
            "( SELECT c.id FROM PrivateComment c WHERE c.post.id = ?1)")
    void deleteByPostId(Long postId);

    @Modifying
    @Query("DELETE FROM PrivateCommentReport p WHERE p.comment.id = ?1")
    void deleteByCommentId(Long commentId);

}

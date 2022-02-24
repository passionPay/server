package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicComment;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicCommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PublicCommentReportRepository extends JpaRepository<PublicCommentReport, Long> {

    boolean existsByCommentAndMember(PublicComment comment, Member member);

    @Modifying
    @Query("DELETE FROM PublicCommentReport p WHERE p.comment.id IN " +
            "( SELECT c.id FROM PublicComment c WHERE c.post.id = ?1)")
    void deleteByPostId(Long postId);

    @Modifying
    @Query("DELETE FROM PublicCommentReport p WHERE p.comment.id = ?1")
    void deleteByCommentId(Long commentId);
}

package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateComment;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateCommentLikeRepository extends JpaRepository<PrivateCommentLike, Long> {
    void deleteByCommentAndMember(PrivateComment privateComment, Member member);
    boolean existsByCommentAndMember(PrivateComment privateComment, Member member);

    @Query("SELECT COUNT(p.id) FROM PrivateCommentLike p WHERE p.comment.id = ?1")
    int getLikeByComment(Long commentId);
}

package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicComment;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicCommentLikeRepository extends JpaRepository<PublicCommentLike, Long> {
    void deleteByCommentAndMember(PublicComment publicComment, Member member);
    boolean existsByCommentAndMember(PublicComment publicComment, Member member);

    @Query("SELECT COUNT(p.id) FROM PublicCommentLike p WHERE p.comment.id = ?1")
    int getLikeByComment(Long commentId);

    @Modifying
    @Query("DELETE FROM PublicCommentLike cl WHERE cl.comment.id IN " +
            "( SELECT c.id FROM PublicComment c WHERE c.post.id = ?1 )")
    void deleteByPostId(Long postId);

    @Modifying
    @Query("DELETE FROM PublicCommentLike cl WHERE cl.comment.id = ?1")
    void deleteByCommentId(Long commentId);
}

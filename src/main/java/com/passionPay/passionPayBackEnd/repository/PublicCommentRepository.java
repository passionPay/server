package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto.PublicCommentInfoDto;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicComment;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PublicCommentRepository extends JpaRepository<PublicComment, Long> {


    List<PublicComment> findByParentComment(PublicComment publicComment);

    @Query("SELECT p FROM PublicComment p WHERE p.post.id = ?1 AND p.parentComment IS NULL")
    List<PublicComment> getParentCommentByPostAndMember(Long postId);

    boolean existsByMemberAndPostAndAnonymous(Member member, PublicPost post, boolean anonymous);

    List<PublicComment> findByMemberAndPostAndAnonymous(Member member, PublicPost post, boolean anonymous, Pageable pageable);


    @Query("SELECT COUNT(DISTINCT p.post.id) FROM PublicComment p WHERE p.member.id = ?1")
    Long getNumPostOfCommented(Long memberId);

    @Modifying
    @Query("UPDATE PublicComment p SET p.deleted = true WHERE p.id = ?1")
    void deleteComment(Long commentId);

    @Modifying
    @Query("UPDATE PublicComment p SET p.content = ?3, p.editedAt = ?2 WHERE p.id = ?1")
    void modifyComment(Long commentId, LocalDateTime editedAt, String content);

    @Modifying
    @Query("UPDATE PublicComment p SET p.likeCount = ?2 WHERE p.id = ?1")
    void modifyLikeCount(Long commentId, Integer likeCount);

    @Modifying
    @Query("UPDATE PublicComment p SET p.parentComment = null WHERE p.post.id = ?1")
    void nullifyParentCommentByPost(Long postId);

    @Modifying
    @Query("DELETE PublicComment p WHERE p.post.id = ?1")
    void deleteCommentByPost(Long postId);

    @Modifying
    @Query("UPDATE PublicComment p SET p.reportCount = ?2 WHERE p.id = ?1")
    void modifyReportCount(Long commentId, Integer reportCount);
}

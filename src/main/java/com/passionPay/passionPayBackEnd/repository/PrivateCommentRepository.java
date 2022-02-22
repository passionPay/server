package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivateCommentInfoDto;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateComment;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrivateCommentRepository extends JpaRepository<PrivateComment, Long> {
    List<PrivateComment> findByMember(Member member);

//    @Query(
//            "SELECT com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivateCommentInfoDto" +
//            "(p.id, p.member.id, p.member.username, p.content, p.createdAt, p.editedAt, p.delete, p.isAnonymous, " +
//                    "com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivateReplyDto()) " + "FROM PrivateComment p WHERE p.post.id = ?1"
//    )
//    List<PrivateCommentInfoDto> getCommentByPost(Long postId);

    List<PrivateComment> findByParentComment(PrivateComment privateComment);

    boolean existsByMemberAndPost(Member member, PrivatePost post);

    boolean existsByMemberAndPostAndAnonymous(Member member, PrivatePost post, boolean anonymous);

    List<PrivateComment> findByMemberAndPostAndAnonymous(Member member, PrivatePost post, boolean anonymous, Pageable pageable);

    Optional<PrivateComment> findOneByMemberAndPostAndAnonymous(Member member, PrivatePost post, boolean anonymous);

    @Query("SELECT COUNT(DISTINCT p.post.id) FROM PrivateComment p WHERE p.member.id = ?1")
    Long getNumPostOfCommented(Long memberId);

    @Modifying
    @Query("UPDATE PrivateComment p SET p.deleted = true WHERE p.id = ?1")
    void deleteComment(Long commentId);

    @Modifying
    @Query("UPDATE PrivateComment p SET p.content = ?3, p.editedAt = ?2 WHERE p.id = ?1")
    void modifyComment(Long commentId, LocalDateTime editedAt, String content);
}

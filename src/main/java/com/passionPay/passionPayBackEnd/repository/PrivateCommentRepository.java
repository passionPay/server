package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivateCommentInfoDto;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateCommentRepository extends JpaRepository<PrivateComment, Long> {
    List<PrivateComment> findByMember(Member member);

//    @Query(
//            "SELECT com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivateCommentInfoDto" +
//            "(p.id, p.member.id, p.member.username, p.content, p.createdAt, p.editedAt, p.isDeleted, p.isAnonymous, " +
//                    "com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivateReplyDto()) " + "FROM PrivateComment p WHERE p.post.id = ?1"
//    )
//    List<PrivateCommentInfoDto> getCommentByPost(Long postId);

    List<PrivateComment> findByParentComment(PrivateComment privateComment);
}

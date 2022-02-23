package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto.PublicPostInfoDto;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicCommunityType;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublicPostRepository extends JpaRepository<PublicPost, Long> {

    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto.PublicPostInfoDto(p.id, p.content, p.title ,p.photoUrl, p.createdAt, p.editedAt, p.member.username, p.member.id, p.anonymous, p.commentCount, p.likeCount, p.communityType) " +
            "FROM PublicPost p " +
            "WHERE p.schoolName = ?1 AND p.communityType = ?2 ORDER BY p.createdAt DESC")
    List<PublicPostInfoDto> getPostBySchoolAndCommunity(String schoolName, PublicCommunityType communityType, Pageable pageable);

    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto.PublicPostInfoDto(p.id, p.content, p.title, p.photoUrl, p.createdAt, p.editedAt, p.member.username, p.member.id, p.anonymous, p.commentCount, p.likeCount, p.communityType) " +
            "FROM PublicPost p WHERE p.member.id = ?1 ORDER BY p.createdAt DESC")
    List<PublicPostInfoDto> getPostByMember(Long MemberId, Pageable pageable);

    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto.PublicPostInfoDto(p.id, p.content, p.title ,p.photoUrl, p.createdAt, p.editedAt, p.member.username, p.member.id, p.anonymous, p.commentCount, p.likeCount, p.communityType) " +
            "FROM PublicPost p WHERE p.communityType = ?1 AND p.member.id = ?2 ORDER BY p.createdAt DESC")
    List<PublicPostInfoDto> getPostByCommunityAndMember(PublicCommunityType communityType, Long MemberId, Pageable pageable);

    @Query("SELECT COUNT(p.id) FROM PublicPost p WHERE p.member.id = ?1")
    Integer getPostCountByMember(Long memberId);


    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto.PublicPostInfoDto(p.id, p.content, p.title ,p.photoUrl, p.createdAt, p.editedAt, p.member.username, p.member.id, p.anonymous, p.commentCount, p.likeCount, p.communityType) " +
            "FROM PublicPost p " +
            "WHERE p.id IN (SELECT DISTINCT c.post.id FROM PublicComment c WHERE c.member.id = ?1) " +
            "ORDER BY p.createdAt DESC")
    List<PublicPostInfoDto> getPostByMemberComment(Long memberId);

    @Modifying
    @Query("UPDATE PublicPost p SET p.anonymousCount = ?1 WHERE p.id = ?2")
    void modifyAnonymousCount(int anonymousCount, Long id);

    @Modifying
    @Query("UPDATE PublicPost p SET p.commentCount = ?1 WHERE p.id = ?2")
    void modifyCommentCount(int commentCount, Long id);

    @Modifying
    @Query("UPDATE PublicPost p SET p.likeCount = ?1 WHERE p.id = ?2")
    void modifyLikeCount(int likeCount, Long id);

    @Modifying
    @Query("UPDATE PublicPost p SET p.content = ?2, p.title = ?3, p.photoUrl = ?4, p.anonymous = ?5, p.editedAt = ?6 WHERE p.id = ?1")
    void modifyPost(Long postId, String content, String title, String photoUrl, boolean anonymous, LocalDateTime editedAt);

    @Modifying
    @Query("DELETE FROM PublicPost p WHERE p.id = ?1")
    void deletePost(Long postId);

}
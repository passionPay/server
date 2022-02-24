package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostInfoDto;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateCommunityType;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrivatePostRepository extends JpaRepository<PrivatePost, Long> {
//    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.(m.id, m.username) FROM Follow f INNER JOIN Member m ON f.follower = m WHERE f.user = ?1")

    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostInfoDto(p.id, p.content, p.title ,p.photoUrl, p.createdAt, p.editedAt, p.member.username, p.member.id ,p.schoolName, p.anonymous, p.commentCount, p.likeCount, p.communityType) " +
            "FROM PrivatePost p " +
            "WHERE p.schoolName = ?1 AND p.communityType = ?2 ORDER BY p.createdAt DESC")
    List<PrivatePostInfoDto> getPostBySchoolAndCommunity(String schoolName, PrivateCommunityType communityType, Pageable pageable);

    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostInfoDto(p.id, p.content, p.title, p.photoUrl, p.createdAt, p.editedAt, p.member.username, p.member.id ,p.schoolName, p.anonymous, p.commentCount, p.likeCount, p.communityType) " +
            "FROM PrivatePost p WHERE p.member.id = ?1 ORDER BY p.createdAt DESC")
    List<PrivatePostInfoDto> getPostByMember(Long MemberId, Pageable pageable);

    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostInfoDto(p.id, p.content, p.title ,p.photoUrl, p.createdAt, p.editedAt, p.member.username, p.member.id ,p.schoolName, p.anonymous, p.commentCount, p.likeCount, p.communityType) " +
            "FROM PrivatePost p WHERE p.communityType = ?1 AND p.member.id = ?2 ORDER BY p.createdAt DESC")
    List<PrivatePostInfoDto> getPostByCommunityAndMember(PrivateCommunityType communityType, Long MemberId, Pageable pageable);

    @Query("SELECT COUNT(p.id) FROM PrivatePost p WHERE p.member.id = ?1")
    Integer getPostCountByMember(Long memberId);

//    @Query("SELECT DISTINCT new com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostInfoDto(p.id, p.content, p.title ,p.photoUrl, p.createdAt, p.member.username, p.member.id ,p.schoolName, p.anonymous, p.commentCount, COUNT(l.id), p.communityType) " +
//            "FROM PrivatePost p INNER JOIN PrivatePostLike l ON l.post = p INNER JOIN PrivateComment c ON c.member.id = ?1 WHERE c.post.id = p.id ORDER BY p.createdAt DESC")

    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivatePostInfoDto(p.id, p.content, p.title ,p.photoUrl, p.createdAt, p.editedAt, p.member.username, p.member.id ,p.schoolName, p.anonymous, p.commentCount, p.likeCount, p.communityType) " +
            "FROM PrivatePost p " +
            "WHERE p.id IN (SELECT DISTINCT c.post.id FROM PrivateComment c WHERE c.member.id = ?1) " +
            "ORDER BY p.createdAt DESC")
    List<PrivatePostInfoDto> getPostByMemberComment(Long memberId);

    @Modifying
    @Query("UPDATE PrivatePost p SET p.anonymousCount = ?1 WHERE p.id = ?2")
    void modifyAnonymousCount(int anonymousCount, Long id);

    @Modifying
    @Query("UPDATE PrivatePost p SET p.commentCount = ?1 WHERE p.id = ?2")
    void modifyCommentCount(int commentCount, Long id);

    @Modifying
    @Query("UPDATE PrivatePost p SET p.likeCount = ?1 WHERE p.id = ?2")
    void modifyLikeCount(int likeCount, Long id);

    @Modifying
    @Query("UPDATE PrivatePost p SET p.content = ?2, p.title = ?3, p.photoUrl = ?4, p.anonymous = ?5, p.editedAt = ?6 WHERE p.id = ?1")
    void modifyPost(Long postId, String content, String title, String photoUrl, boolean anonymous, LocalDateTime editedAt);

    @Modifying
    @Query("DELETE FROM PrivatePost p WHERE p.id = ?1")
    void deletePost(Long postId);

    @Modifying
    @Query("UPDATE PrivatePost p SET p.reportCount = ?2 WHERE p.id = ?1")
    void modifyReportCount(Long postId, Integer reportCount);

}
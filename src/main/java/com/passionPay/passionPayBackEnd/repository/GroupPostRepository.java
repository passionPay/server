package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMember;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupPostRepository extends JpaRepository<GroupPost, Long> {
    // save
    GroupPost save(GroupPost groupPost);

    // find
    Optional<GroupPost> findById(Long groupPostId);

    List<GroupPost> findByGroupMember(GroupMember groupMember);

    @Query("SELECT gp " +
            "FROM GroupPost gp INNER JOIN gp.groupMember gpm " +
            "WHERE gpm.groupId = :groupId " +
            "ORDER BY gpm.groupId DESC")
    List<GroupPost> findByGroupIdByOrderByGroupIdDesc(Long groupId);

    // update post content
    @Modifying(clearAutomatically = true) // 영속성 컨텍스트 초기화
    @Query("UPDATE GroupPost gp " +
            "SET gp.content = :content, gp.photoUrl = :photoUrl " +
            "WHERE gp.groupPostId = :groupPostId")
    int updateById(@Param("content") String content, @Param("photoUrl") String photoUrl, @Param("groupPostId") Long groupPostId);

    // delete
    void deleteById(Long groupPostId);

    void delete(GroupPost groupPost);

    @Modifying
    @Query("DELETE " +
            "FROM GroupPost gp " +
            "WHERE gp.groupMember IN (" +
            "SELECT gm FROM GroupMember gm WHERE gm.groupId = :groupId)")
    void deleteByGroupId(@Param("groupId") Long groupId);
    void deleteByGroupMember(GroupMember groupMember);
}

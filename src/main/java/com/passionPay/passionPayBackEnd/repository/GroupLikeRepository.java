package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupLike;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMember;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupLikeRepository extends JpaRepository<GroupLike, Long> {
    // save
    GroupLike save(GroupLike groupLike);

    // count by post
    @Query("SELECT COUNT(gl.id) " +
            "FROM GroupLike gl INNER JOIN gl.groupPost gp " +
            "WHERE gp = :groupPost")
    Long findCountByGroupPost(@Param("groupPost") GroupPost groupPost);

    // delete
    void delete(GroupLike groupLike);
    void deleteByGroupMember(GroupMember groupMember);
    void deleteByGroupPost(GroupPost groupPost);
}

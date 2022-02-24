package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePost;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivatePostLikeRepository extends JpaRepository<PrivatePostLike, Long> {

    boolean existsByMemberAndPost(Member member, PrivatePost post);
    void deleteByMemberAndPost(Member member, PrivatePost post);

    @Query("SELECT COUNT(p.id) FROM PrivatePostLike p WHERE p.post.id = ?1")
    int likeCountOfPost(Long postId);

    @Modifying
    @Query("DELETE FROM PrivatePostLike p WHERE p.post.id = ?1")
    void deleteByPostId(Long postId);

}

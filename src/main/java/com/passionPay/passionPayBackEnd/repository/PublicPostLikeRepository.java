package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicPost;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicPostLikeRepository extends JpaRepository<PublicPostLike, Long> {

    boolean existsByMemberAndPost(Member member, PublicPost post);
    void deleteByMemberAndPost(Member member, PublicPost post);

    @Query("SELECT COUNT(p.id) FROM PublicPostLike p WHERE p.post.id = ?1")
    int likeCountOfPost(Long postId);

    @Modifying
    @Query("DELETE FROM PublicPostLike p WHERE p.post.id = ?1")
    void deleteByMemberId(Long postId);

}
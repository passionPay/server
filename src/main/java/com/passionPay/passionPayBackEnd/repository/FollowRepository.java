package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.controller.dto.FollowInfoDto;
import com.passionPay.passionPayBackEnd.domain.Follow;
import com.passionPay.passionPayBackEnd.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByUserAndFollowing(Member user, Member following);

    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.FollowInfoDto(m.id, m.username) FROM Follow f INNER JOIN Member m ON f.following = m WHERE f.user = ?1 AND f.valid = true")
    List<FollowInfoDto> getFollowingOBByMember(Member member);

    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.FollowInfoDto(m.id, m.username) FROM Follow f INNER JOIN Member m ON f.user = m WHERE f.following = ?1 AND f.valid = true")
    List<FollowInfoDto> getFollowingIBByMember(Member member);

    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.FollowInfoDto(m.id, m.username) FROM Follow f INNER JOIN Member m ON f.user = m WHERE f.following = ?1 AND f.valid = false")
    List<FollowInfoDto> getFollowingOBRequest(Member member);

    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.FollowInfoDto(m.id, m.username) FROM Follow f INNER JOIN Member m ON f.following = m WHERE f.user = ?1 AND f.valid = false")
    List<FollowInfoDto> getFollowingIBRequest(Member member);

    @Modifying
    @Query("UPDATE Follow f SET f.valid = true WHERE f.id = ?1")
    void acceptFollowRequest(Long followId);

    @Modifying
    @Query("DELETE FROM Follow f WHERE f.user = ?1 OR f.following = ?1")
    void deleteFollowOfUser(Member member);

    @Modifying
    @Query("UPDATE Follow f SET f.valid = true WHERE f.following.id = ?1")
    void validateAllRequest(Long memberId);

}

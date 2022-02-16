package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.controller.dto.FollowInfoDto;
import com.passionPay.passionPayBackEnd.domain.Follow;
import com.passionPay.passionPayBackEnd.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByUserAndFollower(Member user, Member follower);

    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.FollowInfoDto(m.id, m.username) FROM Follow f INNER JOIN Member m ON f.follower = m WHERE f.user = ?1")
    Optional<List<FollowInfoDto> > getFollowingByUser(Member member);

    @Query("SELECT new com.passionPay.passionPayBackEnd.controller.dto.FollowInfoDto(m.id, m.username) FROM Follow f INNER JOIN Member m ON f.user = m WHERE f.follower = ?1")
    Optional<List<FollowInfoDto> > getFollowerByUser(Member member);

    @Modifying
    @Query("DELETE FROM Follow f WHERE f.user = ?1 OR f.follower = ?1")
    void deleteFollowOfUser(Member member);
}

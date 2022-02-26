package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupLike;
import com.passionPay.passionPayBackEnd.repository.GroupLikeRepository;
import com.passionPay.passionPayBackEnd.repository.GroupMemberRepository;
import com.passionPay.passionPayBackEnd.repository.GroupPostRepository;
import com.passionPay.passionPayBackEnd.util.SecurityUtil;
import org.springframework.stereotype.Service;

@Service
public class GroupLikeService {
    private final GroupLikeRepository groupLikeRepository;
    private final GroupPostRepository groupPostRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupLikeService(GroupLikeRepository groupLikeRepository, GroupPostRepository groupPostRepository, GroupMemberRepository groupMemberRepository) {
        this.groupLikeRepository = groupLikeRepository;
        this.groupPostRepository = groupPostRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    // save group like
    public Long save(Long memberId, Long postId) {
        return groupPostRepository.findById(postId)
                .map(groupPost ->
                        // find group post object
                    groupMemberRepository.findByMemberIdAndGroupId(memberId, groupPost.getGroupMember().getGroupId())
                            // find group member object
                            .map(groupMember -> {
                                GroupLike groupLike = GroupLike.builder()
                                        .groupPost(groupPost)
                                        .groupMember(groupMember)
                                        .build();
                                // save group like
                                groupLikeRepository.save(groupLike);
                                return groupLike.getGroupLikeId();
                            }).orElseThrow(RuntimeException::new))
                .orElseThrow(RuntimeException::new);
    }

    // get like count by post id
    public Long getLikeCountByPostId(Long postId) {
        return groupPostRepository.findById(postId)
                .map(groupPost -> groupLikeRepository.findCountByGroupPost(groupPost))
                .orElseThrow(RuntimeException::new);
    }

    // delete group like
    public boolean delete(Long groupLikeId) {
        return groupLikeRepository.findById(groupLikeId)
                .map(groupLike -> {
                    if (groupLike.getGroupMember().getMemberId() != SecurityUtil.getCurrentMemberId())
                        return false;
                    groupLikeRepository.delete(groupLike);
                    return true;
                }).orElse(false);
    }
}

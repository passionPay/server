package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.GroupCommentDto;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupComment;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupPost;
import com.passionPay.passionPayBackEnd.repository.GroupMemberRepository;
import com.passionPay.passionPayBackEnd.repository.GroupPostRepository;
import com.passionPay.passionPayBackEnd.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupCommentService {

    private final com.passionPay.passionPayBackEnd.repository.GroupCommentRepository groupCommentRepository;
    private final GroupPostRepository groupPostRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupCommentService(com.passionPay.passionPayBackEnd.repository.GroupCommentRepository groupCommentRepository, GroupPostRepository groupPostRepository, GroupMemberRepository groupMemberRepository) {
        this.groupCommentRepository = groupCommentRepository;
        this.groupPostRepository = groupPostRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    // save group comment
    public GroupCommentDto save(Long postId, String content, Long memberId) {
        return groupPostRepository.findById(postId)
                .map(groupPost -> {
                    // post
                    GroupComment groupComment = groupMemberRepository
                            .findByMemberIdAndGroupId(memberId, groupPost.getGroupMember().getGroupId())
                            // group member
                            .map(groupMember ->
                                GroupComment.builder()
                                        .content(content)
                                        .groupPost(groupPost)
                                        .groupMember(groupMember)
                                        .build())
                            .orElseThrow(RuntimeException::new);
                    // save
                    groupCommentRepository.save(groupComment);
                    return GroupCommentDto.from(groupComment);
                })
                .orElseThrow(RuntimeException::new);
    }

    // get group comments by post id
    public List<GroupCommentDto> getGroupCommentByPostId(Long postId) {
        return groupPostRepository.findById(postId)
                .map(groupPost ->
                    groupCommentRepository.findByGroupPost(groupPost)
                            // group comment dto 로 변환
                            .stream()
                            .map(GroupCommentDto::from)
                            .collect(Collectors.toList()))
                .orElseThrow(RuntimeException::new);
    }

    // update comment
    public GroupCommentDto updateGroupComment(Long groupCommentId, String content) {
        return groupCommentRepository.findById(groupCommentId)
                .map(groupComment -> {
                  groupComment.setContent(content);
                  return GroupCommentDto.from(groupComment);
                }).orElseThrow(RuntimeException::new);
    }

    // delete comment
    public boolean delete(Long groupCommentId) {
        return groupCommentRepository.findById(groupCommentId)
                .map(groupComment -> {
                    if (groupComment.getGroupMember().getMemberId() != SecurityUtil.getCurrentMemberId())
                        return false;
                    // 연관관계 제거
                    groupCommentRepository.delete(groupComment);
                    return true;
                }).orElseThrow(RuntimeException::new);
    }
}

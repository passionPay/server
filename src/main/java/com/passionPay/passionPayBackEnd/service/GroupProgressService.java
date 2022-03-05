package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.GroupProgressDto;
import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.Group;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupProgress;
import com.passionPay.passionPayBackEnd.repository.GroupMemberRepository;
import com.passionPay.passionPayBackEnd.repository.GroupMissionRepository;
import com.passionPay.passionPayBackEnd.repository.GroupProgressRepository;
import com.passionPay.passionPayBackEnd.util.SecurityUtil;
import org.springframework.stereotype.Service;

@Service
public class GroupProgressService {

    private final GroupProgressRepository groupProgressRepository;
    private final GroupMissionRepository groupMissionRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupProgressService(GroupProgressRepository groupProgressRepository, GroupMissionRepository groupMissionRepository, GroupMemberRepository groupMemberRepository) {
        this.groupProgressRepository = groupProgressRepository;
        this.groupMissionRepository = groupMissionRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    // save
    public GroupProgressDto save(Long memberId, Long groupMissionId) {
        return groupMissionRepository.findById(groupMissionId)
                .map(groupMission ->
                    groupMemberRepository.findByMemberIdAndGroupId(memberId, groupMission.getGroup().getGroupId())
                            .map(groupMember -> {
                                GroupProgress groupProgress = GroupProgress.builder()
                                        .groupMember(groupMember)
                                        .complete(false)
                                        .groupMission(groupMission)
                                        .build();
                                groupProgressRepository.save(groupProgress);
                                return GroupProgressDto.builder()
                                        .groupProgressId(groupProgress.getGroupProgressId())
                                        .memberInfo(MemberInfoDto.of(groupMember.getMember()))
                                        .completed(groupProgress.isComplete())
                                        .missionName(groupProgress.getGroupMission().getMissionName())
                                        .build();
                            }).orElseThrow(RuntimeException::new))
                .orElseThrow(RuntimeException::new);
    }

    // find
    public int getMyCount(Group group) {
        return groupMemberRepository.findByMemberIdAndGroupId(SecurityUtil.getCurrentMemberId(), group.getGroupId())
                .map(groupMember -> {
                    // 내가 성공한 미션 개수
                    int count = groupProgressRepository.findCountByGroupMemberAndGroupMission(groupMember.getGroupMemberId(), group);
                    // 미션 개수 총 합
                    int missionCount = groupMissionRepository.findByGroup(group).size();
                    return count / missionCount * 100;
                }).orElseThrow(RuntimeException::new);
    }

    public int getGroupAvgCount(Group group) {
        int count = groupProgressRepository.findCountByGroupMission(group);
        int missionCount = groupMissionRepository.findByGroup(group).size();
        int memberCount = group.getGroupMembers().size();
        return (count / missionCount) / memberCount * 100;
    }

    // update
    public GroupProgressDto update(Long groupProgressId, boolean complete) {
        return groupProgressRepository.findById(groupProgressId)
                .map(groupProgress -> {
                    if (groupProgress.getGroupMember().getMemberId() != SecurityUtil.getCurrentMemberId())
                        throw new RuntimeException("접근 권한 없음");
                    groupProgress.setComplete(complete);
                    groupProgressRepository.save(groupProgress);
                    return GroupProgressDto.builder()
                            .missionName(groupProgress.getGroupMission().getMissionName())
                            .groupProgressId(groupProgress.getGroupProgressId())
                            .completed(groupProgress.isComplete())
                            .memberInfo(MemberInfoDto.of(groupProgress.getGroupMember().getMember()))
                            .build();
                }).orElseThrow(RuntimeException::new);
    }

    // delete
    public boolean delete(Long groupProgressId) {
        return groupProgressRepository.findById(groupProgressId)
                .map(groupProgress -> {
                    groupProgressRepository.delete(groupProgress);
                    return true;
                }).orElse(false);
    }
}

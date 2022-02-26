package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMission;
import com.passionPay.passionPayBackEnd.repository.GroupMissionRepository;
import com.passionPay.passionPayBackEnd.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMissionService {
    private final GroupMissionRepository groupMissionRepository;
    private final GroupRepository groupRepository;

    public GroupMissionService(GroupMissionRepository groupMissionRepository, GroupRepository groupRepository) {
        this.groupMissionRepository = groupMissionRepository;
        this.groupRepository = groupRepository;
    }

    // group mission 추가
    public GroupMission save(Long groupId, String missionName) {
        return groupRepository.findById(groupId)
                .map(group -> {
                    GroupMission groupMission = GroupMission.builder()
                            .missionName(missionName)
                            .group(group)
                            .build();
                    return groupMissionRepository.save(groupMission);
                }).orElseThrow(RuntimeException::new);
    }

    // group mission 조회
    public List<GroupMission> findGroupMissionsByGroupId(Long groupMissionId) {
        return groupRepository.findById(groupMissionId)
                .map(groupMissionRepository::findByGroup)
                .orElseThrow(RuntimeException::new);
    }

    // group mission 수정
    public GroupMission updateGroupMission(Long groupMissionId, String missionName) {
        return groupMissionRepository.findById(groupMissionId)
                .map(groupMission -> {
                    groupMission.setMissionName(missionName);
                    return groupMissionRepository.save(groupMission);
                }).orElseThrow(RuntimeException::new);
    }

    // group mission 삭제
    public boolean deleteGroupMissionById(Long groupMissionId) {
        return groupMissionRepository.findById(groupMissionId)
                .map(groupMission -> {
                    groupMissionRepository.delete(groupMission);
                    return true;
                }).orElse(false);
    }
}

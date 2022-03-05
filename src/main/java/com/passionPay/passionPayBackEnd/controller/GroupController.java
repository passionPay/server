package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.GroupDetailDto;
import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.GroupInfoDto;
import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.GroupRequestDto;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMission;
import com.passionPay.passionPayBackEnd.service.GroupMissionService;
import com.passionPay.passionPayBackEnd.service.GroupService;
import com.passionPay.passionPayBackEnd.util.DateUtil;
import com.passionPay.passionPayBackEnd.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;
    private final GroupMissionService groupMissionService;

    public GroupController(GroupService groupService, GroupMissionService groupMissionService) {
        this.groupService = groupService;
        this.groupMissionService = groupMissionService;
    }

    @GetMapping
    public ResponseEntity<GroupDetailDto> getAllGroup(@RequestBody String today) {
        return ResponseEntity.ok(groupService.getGroupInfoList(SecurityUtil.getCurrentMemberId(), DateUtil.parseStringToDate(today)));
    }

    // group 과 연관된 mission 조회
    @GetMapping("/{groupId}/mission")
    public ResponseEntity<List<GroupMission>> getGroupGoalsByGroupId(@PathVariable(name="groupId") Long groupId) {
        return ResponseEntity.ok(groupMissionService.findGroupMissionsByGroupId(groupId));
    }

    // group 생성
    @PostMapping
    public ResponseEntity<GroupInfoDto> saveGroup(@RequestBody GroupRequestDto groupRequestDto) {
        return ResponseEntity.ok(groupService.saveGroup(groupRequestDto));
    }

    // group id로 group 조회
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupInfoDto> getGroupInfoByGroupId(@PathVariable(name="groupId") Long groupId) {
        return ResponseEntity.ok(groupService.getGroupInfoById(groupId));
    }

    // group 에 member 추가
    @PostMapping("/{groupId}/register")
    public ResponseEntity<GroupInfoDto> joinGroupByGroupId(
            @PathVariable(name="groupId") Long groupId) {
        System.out.println(groupId);
        return ResponseEntity.ok(groupService.joinGroupByGroupId(groupId, SecurityUtil.getCurrentMemberId()));
    }

    // group mission 추가
    @PostMapping("/{groupId}/mission")
    public ResponseEntity<GroupMission> addGroupMission(
            @PathVariable(name="groupId") Long groupId, @RequestBody String missionName) {
        return ResponseEntity.ok(groupMissionService.save(groupId, missionName));
    }

    // group 수정
    @PutMapping("/{groupId}")
    public ResponseEntity<GroupInfoDto> updateGroup(
            @PathVariable(name="groupId") Long groupId, @RequestBody GroupRequestDto groupRequestDto) {
        return ResponseEntity.ok(groupService.updateGroup(groupId, groupRequestDto));
    }

    // group mission 수정
    @PutMapping("/mission/{groupMissionId}")
    public ResponseEntity<GroupMission> updateGroupMission(
            @PathVariable(name="groupMissionId") Long groupMissionId, @RequestBody String missionName) {
        return ResponseEntity.ok(groupMissionService.updateGroupMission(groupMissionId, missionName));
    }

    // group mission 삭제
    @DeleteMapping("/mission/{groupMissionId}")
    public ResponseEntity<Boolean> deleteGoal(
            @PathVariable(name="groupMissionId") Long groupMissionId) {
        return ResponseEntity.ok(groupMissionService.deleteGroupMissionById(groupMissionId));
    }

    // group 삭제
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Boolean> deleteGroup(@PathVariable(name="groupId") Long groupId) {
        return ResponseEntity.ok(groupService.deleteGroup(groupId));
    }

    // group 탈퇴
    @DeleteMapping("/{groupId}/unregister")
    public ResponseEntity<Boolean> deleteGroupMember(
            @PathVariable(name="groupId") Long groupId) {
        return ResponseEntity.ok(groupService.deleteGroupMember(groupId, SecurityUtil.getCurrentMemberId()));
    }
}

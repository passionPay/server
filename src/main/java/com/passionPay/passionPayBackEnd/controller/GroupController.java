package com.passionPay.passionPayBackEnd.controller;

import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.GroupInfoDto;
import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.GroupRequestDto;
import com.passionPay.passionPayBackEnd.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/")
    public ResponseEntity<List<GroupInfoDto>> getAllGroup() {
        return ResponseEntity.ok(groupService.getGroupInfoList());
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupInfoDto> getGroupInfoByGroupId(@RequestParam(name="groupId") Long groupId) {
        return ResponseEntity.ok(groupService.getGroupInfoById(groupId));
    }

    @PostMapping("/")
    public ResponseEntity<GroupInfoDto> saveGroup(@RequestBody GroupRequestDto groupRequestDto) {
        return ResponseEntity.ok(groupService.saveGroup(groupRequestDto));
    }
}

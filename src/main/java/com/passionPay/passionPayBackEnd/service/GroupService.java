package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.GroupInfoDto;
import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.GroupRequestDto;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.Group;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMember;
import com.passionPay.passionPayBackEnd.repository.GroupMemberRepository;
import com.passionPay.passionPayBackEnd.repository.GroupRepository;
import com.passionPay.passionPayBackEnd.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupService(GroupRepository groupRepository, MemberRepository memberRepository, GroupMemberRepository groupMemberRepository) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    // group 생성
    public GroupInfoDto saveGroup(GroupRequestDto groupRequestDto) {
        return memberRepository.findById(groupRequestDto.getMemberId())
                .map(member -> {
                    Group group = GroupRequestDto.from(groupRequestDto);
                    GroupMember groupMember = GroupMember.builder()
                            .group(group)
                            .member(member)
                            .build();
                    groupRepository.save(group);
                    groupMemberRepository.save(groupMember);
                    return GroupInfoDto.from(group);
                })
                .orElseThrow(RuntimeException::new);
    }

    // 모든 group 조회
    public List<GroupInfoDto> getGroupInfoList() {
        return groupRepository.findAllOrderByGroupId()
                .stream().map(GroupInfoDto::from)
                .collect(Collectors.toList());
    }

    // group id 로 group info 조회
    public GroupInfoDto getGroupInfoById(Long groupId) {
        return groupRepository.findById(groupId)
                .map(GroupInfoDto::from)
                .orElseThrow(RuntimeException::new);
    }
}

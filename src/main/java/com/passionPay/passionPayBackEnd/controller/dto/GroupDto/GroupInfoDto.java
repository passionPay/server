package com.passionPay.passionPayBackEnd.controller.dto.GroupDto;

import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.Group;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMission;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupInfoDto {
    private Long groupId;
    private String groupName;
    private String groupDescription;
    private boolean groupPrivacy;
    private List<GroupMission> groupMissions;
    private int groupTimeGoal;
    private List<MemberInfoDto> groupMember;
    private int maxMember;

    public static GroupInfoDto from(Group group, List<GroupMission> groupMissions) {
        List<MemberInfoDto> groupMemberInfoList = group.getGroupMembers()
                .stream().map(member -> MemberInfoDto.builder()
                        .displayName(member.getMember().getDisplayName())
                        .categoryName(member.getMember().getCategoryName())
                        .activated(member.getMember().isActivated())
                        .email(member.getMember().getEmail())
                        .grade(member.getMember().getGrade())
                        .photoUrl(member.getMember().getPhotoUrl())
                        .stage(member.getMember().getStage())
                        .username(member.getMember().getUsername())
                        .build())
                .collect(Collectors.toList());

        return GroupInfoDto.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .groupDescription(group.getGroupDescription())
                .groupPrivacy(!group.getGroupPassword().isBlank()) // 공개여부
                .groupMissions(groupMissions)
                .groupTimeGoal(group.getGroupTimeGoal())
                .groupMember(groupMemberInfoList)
                .maxMember(group.getMaxMember())
                .build();
    }
}

package com.passionPay.passionPayBackEnd.controller.dto.GroupDto;

import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.Group;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMember;
import com.passionPay.passionPayBackEnd.util.DateUtil;
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
    private String groupGoal;
    private int groupTimeGoal;
    private List<MemberInfoDto> groupMember;
    private int maxMember;

    public static GroupInfoDto from(Group group) {
        List<MemberInfoDto> groupMemberInfoList = group.getMembers()
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
                .groupGoal(group.getGroupGoal())
                .groupTimeGoal(group.getGroupTimeGoal())
                .groupMember(groupMemberInfoList)
                .maxMember(group.getMaxMember())
                .build();
    }
}

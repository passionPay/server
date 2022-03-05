package com.passionPay.passionPayBackEnd.controller.dto.GroupDto;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.Group;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMission;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyGroupDto {
    private Long groupId;
    private String groupName;
    private String groupDescription;
    private boolean groupPrivacy;
    private List<GroupMission> groupMissions;
    private List<String> groupMembers;
    private int maxMember;
    private String groupPassword;
    private ProgressDto myGoalProgresses;

    public static MyGroupDto from(Group group, ProgressDto progress) {
        List<String> groupMembers = group.getGroupMembers()
                .stream()
                .map(groupMember ->
                    groupMember.getMember().getPhotoUrl())
                .collect(Collectors.toList());

        return MyGroupDto.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .groupDescription(group.getGroupDescription())
                .groupPrivacy(!group.getGroupPassword().isBlank())
                .maxMember(group.getMaxMember())
                .groupMembers(groupMembers)
                .groupPassword(group.getGroupPassword())
                .myGoalProgresses(progress)
                .build();
    }
}

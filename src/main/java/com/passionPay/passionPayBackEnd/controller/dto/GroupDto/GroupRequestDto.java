package com.passionPay.passionPayBackEnd.controller.dto.GroupDto;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.Group;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupRequestDto {
    private String groupName;
    private String groupDescription;
    private int groupTimeGoal;
    private int maxMember;
    private List<String> groupMissions;
    private boolean groupPrivacy;
    private String groupPassword;

    public static Group from(GroupRequestDto groupRequestDto) {
        return Group.builder()
                .groupName(groupRequestDto.getGroupName())
                .groupTimeGoal(groupRequestDto.getGroupTimeGoal())
                .groupPassword(groupRequestDto.getGroupPassword())
                .groupDescription(groupRequestDto.getGroupDescription())
                .maxMember(groupRequestDto.getMaxMember())
                .build();
    }
}

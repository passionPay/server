package com.passionPay.passionPayBackEnd.controller.dto.GroupDto;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMission;
import lombok.*;

import java.util.List;

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
    private ProgressDto myGoalProgresses;
}

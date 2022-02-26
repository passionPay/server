package com.passionPay.passionPayBackEnd.controller.dto.GroupDto;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMission;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OtherGroupDto {
    private Long groupId;
    private String groupName;
    private String groupDescription;
    private boolean groupPrivacy;
    private List<GroupMission> groupMissions;
    private int groupAvgStudyTime;
    private int maxMember;
    private ProgressDto groupAvgGoalProgresses;
}

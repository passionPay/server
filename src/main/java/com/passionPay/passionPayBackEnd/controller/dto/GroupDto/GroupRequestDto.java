package com.passionPay.passionPayBackEnd.controller.dto.GroupDto;

import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.Group;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMember;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupRequestDto {
    private String groupName;
    private String groupGoal;
    private int groupTimeGoal;
    private Long memberId; // 그룹 생성자
    private int maxMember;
    private String password;

    public static Group from(GroupRequestDto groupRequestDto) {
        return Group.builder()
                .groupGoal(groupRequestDto.getGroupGoal())
                .groupName(groupRequestDto.getGroupName())
                .groupTimeGoal(groupRequestDto.getGroupTimeGoal())
                .groupPassword(groupRequestDto.getPassword())
                .maxMember(groupRequestDto.getMaxMember())
                .build();
    }
}

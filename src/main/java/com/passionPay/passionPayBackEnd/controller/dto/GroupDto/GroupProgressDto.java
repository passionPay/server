package com.passionPay.passionPayBackEnd.controller.dto.GroupDto;

import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupProgressDto {
    private long groupProgressId;
    private String missionName;
    private MemberInfoDto memberInfo;
    private boolean completed;
}

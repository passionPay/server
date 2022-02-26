package com.passionPay.passionPayBackEnd.controller.dto.PlannerDto;

import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.GroupCommentDto;
import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupComment;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.PlannerComment;
import com.passionPay.passionPayBackEnd.util.DateUtil;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannerCommentDto {
    private Long plannerCommentId;
    private String content;
    private MemberInfoDto memberInfo;
    private String createdAt;

    public static PlannerCommentDto from(PlannerComment plannerComment) {
        MemberInfoDto memberInfo = MemberInfoDto.of(plannerComment.getMember());
        return PlannerCommentDto.builder()
                .plannerCommentId(plannerComment.getId())
                .content(plannerComment.getContent())
                .createdAt(DateUtil.formatDateTimeToString(plannerComment.getCreatedAt()))
                .memberInfo(memberInfo)
                .build();
    }
}

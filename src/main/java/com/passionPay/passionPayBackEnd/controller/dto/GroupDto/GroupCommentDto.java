package com.passionPay.passionPayBackEnd.controller.dto.GroupDto;

import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupComment;
import com.passionPay.passionPayBackEnd.util.DateUtil;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupCommentDto {
    private Long groupCommentId;
    private String content;
    private MemberInfoDto memberInfo;
    private String createdAt;

    public static GroupCommentDto from(GroupComment groupComment) {
        MemberInfoDto memberInfo = MemberInfoDto.of(groupComment.getGroupMember().getMember());
        return GroupCommentDto.builder()
                .groupCommentId(groupComment.getGroupCommentId())
                .memberInfo(memberInfo)
                .content(groupComment.getContent())
                .createdAt(DateUtil.formatDateTimeToString(groupComment.getCreatedAt()))
                .build();
    }
}

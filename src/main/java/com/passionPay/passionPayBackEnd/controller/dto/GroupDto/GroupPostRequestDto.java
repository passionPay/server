package com.passionPay.passionPayBackEnd.controller.dto.GroupDto;

import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMember;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupPost;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupPostRequestDto {
    private Long memberId;
    private String content;
    private String photoUrl;

    public static GroupPost from(GroupPostRequestDto groupPostRequestDto, GroupMember groupMember) {
        return GroupPost.builder()
                .content(groupPostRequestDto.getContent())
                .groupMember(groupMember)
                .photoUrl(groupPostRequestDto.getPhotoUrl())
                .build();
    }
}

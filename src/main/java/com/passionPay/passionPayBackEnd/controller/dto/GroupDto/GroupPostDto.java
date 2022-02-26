package com.passionPay.passionPayBackEnd.controller.dto.GroupDto;

import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupPost;
import com.passionPay.passionPayBackEnd.util.DateUtil;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupPostDto {
    private long postId;
    private MemberInfoDto memberInfo;
    private String content;
    private String photoUrl;
    private String createdAt;

    public static GroupPostDto from(GroupPost groupPost) {
        MemberInfoDto memberInfoDto = MemberInfoDto.of(groupPost.getGroupMember().getMember());

        return GroupPostDto.builder()
                .postId(groupPost.getGroupPostId())
                .memberInfo(memberInfoDto)
                .content(groupPost.getContent())
                .photoUrl(groupPost.getPhotoUrl())
                .createdAt(DateUtil.formatDateTimeToString(groupPost.getCreatedAt()))
                .build();
    }
}

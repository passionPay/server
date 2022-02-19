package com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto;

import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateCommunityType;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivatePost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivatePostDto {

    private String content;
    private String title;
    private String photoUrl;
    private Long memberId;
    private String schoolName;
    private boolean anonymous;
    private PrivateCommunityType communityType;

}

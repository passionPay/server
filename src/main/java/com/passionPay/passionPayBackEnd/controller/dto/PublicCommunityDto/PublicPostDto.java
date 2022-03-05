package com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto;

import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicCommunityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicPostDto {

    private String content;
    private String title;
    private String photoUrl;
    private Long memberId;
    private boolean anonymous;
    private PublicCommunityType communityType;

}

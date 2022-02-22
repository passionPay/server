package com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto;

import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateCommunityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivatePostModifyDto {

    private String content;
    private String title;
    private String photoUrl;
    private boolean anonymous;

}

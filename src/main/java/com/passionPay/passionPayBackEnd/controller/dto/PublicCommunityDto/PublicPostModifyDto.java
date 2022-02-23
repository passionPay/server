package com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicPostModifyDto {

    private String content;
    private String title;
    private String photoUrl;
    private boolean anonymous;

}

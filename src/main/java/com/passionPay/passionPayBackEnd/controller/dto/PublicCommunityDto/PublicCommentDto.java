package com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicCommentDto {

    private Long postId;
    private Long memberId;
    private String content;
    private boolean anonymous;
    private Long parentCommentId;
}

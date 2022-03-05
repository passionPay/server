package com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicReplyDto {
    private Long id;
    private Long memberId;
    private String memberName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private boolean deleted;
    private boolean anonymous;
    private Integer anonymousCount;
    private Integer likeCount;
    private boolean likedByMe;
}

package com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivateCommentInfoDto {
    private Long id;
    private Long memberId;
    private String memberName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private boolean deleted;
    private boolean anonymous;
    private Integer anonymousCount;
    private List<PrivateReplyDto> reply;
}

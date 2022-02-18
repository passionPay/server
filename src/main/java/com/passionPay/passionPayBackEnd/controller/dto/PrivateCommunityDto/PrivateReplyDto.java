package com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivateReplyDto {
    private Long id;
    private Long memberId;
    private String memberName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private boolean isDeleted;
    private boolean isAnonymous;
}

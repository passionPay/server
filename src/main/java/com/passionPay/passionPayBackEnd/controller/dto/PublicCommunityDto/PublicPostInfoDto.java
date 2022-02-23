package com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto;

import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicCommunityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicPostInfoDto {

    private Long id;
    private String content;
    private String title;
    private String photoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private String memberName;
    private Long memberId;
    private boolean anonymous;
    private int commentCount;
    private int likeCount;
    private PublicCommunityType communityType;

}

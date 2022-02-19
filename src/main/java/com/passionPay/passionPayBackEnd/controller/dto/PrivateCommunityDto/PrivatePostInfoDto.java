package com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto;

import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.PrivateCommunityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivatePostInfoDto {

    private Long id;
    private String content;
    private String title;
    private String photoUrl;
    private LocalDateTime createdAt;
    private String memberName;
    private Long memberId;
    private String schoolName;
    private boolean anonymous;
    private PrivateCommunityType communityType;

}

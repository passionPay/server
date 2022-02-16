package com.passionPay.passionPayBackEnd.controller.dto;

import com.passionPay.passionPayBackEnd.domain.Follow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowInfoDto {
    private Long id;
    private String username;


    public static FollowInfoDto of(Follow follow) {
        return FollowInfoDto.builder()
                .id(follow.getId())
                .username(follow.getUser().getUsername())
                .build();
    }


}

package com.passionPay.passionPayBackEnd.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileInfoDto {

    private long following;
    private long follower;
    private long myPost;
    private long myComment;

}

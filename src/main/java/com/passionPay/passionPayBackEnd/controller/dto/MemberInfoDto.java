package com.passionPay.passionPayBackEnd.controller.dto;


import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.Stage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoDto {
    private String username;
    private String email;
    private String displayName;
    private boolean activated;
    private String photoUrl;
    private String categoryName;
    private Stage stage;
    private int grade;

    public static MemberInfoDto of(Member member) {
        return MemberInfoDto.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .displayName(member.getDisplayName())
                .activated(member.isActivated())
                .photoUrl(member.getPhotoUrl())
                .categoryName(member.getCategoryName())
                .stage(member.getStage())
                .grade(member.getGrade())
                .build();
    }
}
package com.passionPay.passionPayBackEnd.controller.dto;


import com.passionPay.passionPayBackEnd.domain.Authority;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    private String username;
    private String password;
    private String email;
    private String displayName;
    private boolean activated;
    private String photoUrl;
    private String categoryName;
    private Stage stage;
    private int grade;



    public Member toMember(PasswordEncoder passwordEncoder) {
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .displayName(displayName)
                .activated(activated)
                .photoUrl(photoUrl)
                .categoryName(categoryName)
                .authority(Authority.ROLE_USER)
                .stage(stage)
                .grade(grade)
                .build();
        return member;
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }

}

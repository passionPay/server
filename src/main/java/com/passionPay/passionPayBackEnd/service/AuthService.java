package com.passionPay.passionPayBackEnd.service;


import com.passionPay.passionPayBackEnd.controller.dto.*;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.RefreshToken;
import com.passionPay.passionPayBackEnd.jwt.TokenProvider;
import com.passionPay.passionPayBackEnd.repository.FollowRepository;
import com.passionPay.passionPayBackEnd.repository.MemberRepository;
import com.passionPay.passionPayBackEnd.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public Long signup(MemberRequestDto memberRequestDto) {
        System.out.println(memberRequestDto.getUsername());
        if (memberRepository.existsByUsername(memberRequestDto.getUsername())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public TokenDto login(MemberRequestDto memberRequestDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
//        RefreshToken refreshToken = RefreshToken.builder()
//                .key(authentication.getName())
//                .value(tokenDto.getRefreshToken())
//                .build();

        RefreshToken refreshToken = new RefreshToken(authentication.getName(), tokenDto.getRefreshToken());


        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKeyValue(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    @Transactional
    public Long getIdByUsername(MemberRequestDto memberRequestDto) {
        Optional<Member> member = memberRepository.findByUsername(memberRequestDto.getUsername());
        if(member.isEmpty()) throw new RuntimeException("이미 가입되어 있는 유저입니다");
        else {
            return member.get().getId();
        }
    }

    @Transactional
    public MemberInfoDto getUserInfoById(Long id) {
        Optional<Member> opMember = memberRepository.findById(id);
        if(opMember.isEmpty()) throw new RuntimeException("이미 가입되어 있는 유저입니다");
        else {
            Member member = opMember.get();
            return MemberInfoDto.of(member);
        }
    }

    @Transactional
    public Long modifyUserById(Long id, MemberRequestDto memberRequestDto) {
        Optional<Member> opMember = memberRepository.findById(id);
        if(opMember.isEmpty()) throw new RuntimeException("이미 가입되어 있는 유저입니다");
        else {
            Member member = opMember.get();

            if(member.isPersonal() == true && memberRequestDto.isPersonal() == false) {
                followRepository.validateAllRequest(id);
            }

            member.setUsername(memberRequestDto.getUsername());
            member.setEmail(memberRequestDto.getEmail());
            member.setDisplayName(memberRequestDto.getDisplayName());
            member.setActivated(memberRequestDto.isActivated());
            member.setPhotoUrl(memberRequestDto.getPhotoUrl());
            member.setSchoolName(memberRequestDto.getSchoolName());
            member.setStage(memberRequestDto.getStage());
            member.setGrade(memberRequestDto.getGrade());
            member.setPersonal(memberRequestDto.isPersonal());

            memberRepository.save(member);
            return id;
        }
    }

    @Transactional
    public Long deleteUserById(Long id) {
        if(memberRepository.existsById(id)) {
            followRepository.deleteFollowOfUser(memberRepository.findById(id).get());
            memberRepository.deleteById(id);
            return id;
        }
        else {
            throw new RuntimeException("cannot delete and non-existent user");
        }
    }

    @Transactional
    public Long modifyPassword(Long memberId, PasswordModifyDto passwordModifyDto) {
        if(memberRepository.existsById(memberId)) {
            Member member = memberRepository.getById(memberId);

            UsernamePasswordAuthenticationToken authenticationToken = passwordModifyDto.toAuthentication();
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);



            member.setPassword(passwordEncoder.encode(passwordModifyDto.getNewPassword()));
            return memberId;
        }
        else {
            throw new RuntimeException("invalid memberId");
        }
    }

}

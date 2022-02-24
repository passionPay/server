package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.*;
import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.PrivateCommentInfoDto;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.*;
import com.passionPay.passionPayBackEnd.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateService {

    private final MemberRepository memberRepository;
    private final PrivatePostRepository privatePostRepository;
    private final PrivatePostLikeRepository privatePostLikeRepository;
    private final PrivateCommentRepository privateCommentRepository;
    private final PrivateCommentLikeRepository privateCommentLikeRepository;
    private final PrivatePostReportRepository privatePostReportRepository;
    private final PrivateCommentReportRepository privateCommentReportRepository;

    /*
     *
     * 게시글 기능
     *
     */

    @Transactional
    public Long addPost(PrivatePostDto privatePostDto) {

        Optional<Member> opMember = memberRepository.findById(privatePostDto.getMemberId());

        if(opMember.isEmpty()) {
            throw new RuntimeException("Wrong user Id!");
        }

        PrivatePost privatePost = PrivatePost.builder()
                .content(privatePostDto.getContent())
                .title(privatePostDto.getTitle())
                .photoUrl(privatePostDto.getPhotoUrl())
                .member(opMember.get())
                .schoolName(privatePostDto.getSchoolName())
                .anonymous(privatePostDto.isAnonymous())
                .communityType(privatePostDto.getCommunityType())
                .anonymousCount(1)
                .commentCount(0)
                .likeCount(0)
                .reportCount(0)
                .build();

        privatePostRepository.save(privatePost);
        return  privatePost.getId();
    }

    @Transactional
    public List<PrivatePostInfoDto> getPostBySchoolAndCommunity(String schoolName, PrivateCommunityType communityType, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return privatePostRepository.getPostBySchoolAndCommunity(schoolName, communityType, pageable);
    }

    @Transactional
    public List<PrivatePostInfoDto> getPostByMember(Long memberId, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return privatePostRepository.getPostByMember(memberId, pageable);
    }

    @Transactional
    public List<PrivatePostInfoDto> getPostByCommunityAndMember(PrivateCommunityType communityType, Long memberId, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return privatePostRepository.getPostByCommunityAndMember(communityType, memberId, pageable);
    }

    @Transactional
    public Integer getPostCountByMember(Long memberId) {
        return privatePostRepository.getPostCountByMember(memberId);
    }

    @Transactional
    public Integer modifyPost(Long postId, PrivatePostModifyDto privatePostModifyDto) {
        return privatePostRepository.findById(postId).map(privatePost -> {
            LocalDateTime editedAt = LocalDateTime.now();
            privatePostRepository.modifyPost(postId, privatePostModifyDto.getContent(), privatePostModifyDto.getTitle(), privatePostModifyDto.getPhotoUrl(), privatePostModifyDto.isAnonymous(), editedAt);
            return 1;
        }).orElseThrow(() -> new RuntimeException("invalid postId"));
    }

    @Transactional
    public Integer deletePost(Long postId) {
        return privatePostRepository.findById(postId).map(privatePost -> {

            privatePostRepository.deleteById(postId);
            privatePostLikeRepository.deleteByPostId(postId);
            privatePostReportRepository.deleteByPostId(postId);

            privateCommentLikeRepository.deleteByPostId(postId);
            privateCommentReportRepository.deleteByPostId(postId);
            privateCommentRepository.nullifyParentCommentByPost(postId);
            privateCommentRepository.deleteCommentByPost(postId);

            return 1;
        }).orElseThrow(() -> new RuntimeException("invalid postId"));
    }

    /*
     * 게시글 좋아요 기능
     */

    @Transactional
    public Long addPostLike(Long postId, Long memberId){
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<PrivatePost> optionalPrivatePost = privatePostRepository.findById(postId);

        if(optionalMember.isEmpty() || optionalPrivatePost.isEmpty()) {
            throw new RuntimeException("invalid post or member Id!!");
        }
        else {

            if(privatePostLikeRepository.existsByMemberAndPost(optionalMember.get(), optionalPrivatePost.get())) {
                throw new RuntimeException("already existing like!!");
            }

            if(Objects.equals(optionalPrivatePost.get().getMember().getId(), memberId)){
                throw new RuntimeException("can't like your own post");
            }

            PrivatePostLike privatePostLike = PrivatePostLike.builder()
                    .member(optionalMember.get())
                    .post(optionalPrivatePost.get())
                    .build();
            privatePostLikeRepository.save(privatePostLike);
            int likeCount = optionalPrivatePost.get().getLikeCount();
            privatePostRepository.modifyLikeCount(likeCount + 1, postId);
            return privatePostLike.getId();
        }
    }

    @Transactional
    public Long deletePostLike(Long postId, Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<PrivatePost> optionalPrivatePost = privatePostRepository.findById(postId);

        if(optionalMember.isEmpty() || optionalPrivatePost.isEmpty()) {
            throw new RuntimeException("invalid post or member Id!!");
        }
        else {

            if(privatePostLikeRepository.existsByMemberAndPost(optionalMember.get(), optionalPrivatePost.get())) {
                privatePostLikeRepository.deleteByMemberAndPost(optionalMember.get(), optionalPrivatePost.get());
                int likeCount = optionalPrivatePost.get().getLikeCount();
                privatePostRepository.modifyLikeCount(likeCount - 1, postId);
                return postId;
            }
            else {
                throw new RuntimeException("like doesn't exist!!");
            }

        }
    }

    @Transactional
    public Boolean isUserLikesPost(Long postId, Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<PrivatePost> optionalPrivatePost = privatePostRepository.findById(postId);

        if(optionalMember.isEmpty() || optionalPrivatePost.isEmpty()) {
            throw new RuntimeException("invalid post or member Id!!");
        }
        else {
            return privatePostLikeRepository.existsByMemberAndPost(optionalMember.get(), optionalPrivatePost.get());
        }

    }

    @Transactional
    public int likeCountOfPost(Long postId) {
        Optional<PrivatePost> optionalPrivatePost = privatePostRepository.findById(postId);

        if(optionalPrivatePost.isEmpty()) {
            throw new RuntimeException("non-existent post!!");
        }
        else {
            return optionalPrivatePost.get().getLikeCount();
        }

    }

    /*
     * 게시글 신고 기능
     */

    @Transactional
    public Integer reportPost(Long memberId, Long postId) {
        if(memberRepository.existsById(memberId) && privatePostRepository.existsById(postId)){
            Member member = memberRepository.getById(memberId);
            PrivatePost privatePost = privatePostRepository.getById(postId);

            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(now, member.getReportLastIssuedAt());

            if(Objects.equals(privatePost.getMember().getId(), memberId)) {
                throw new RuntimeException("can't report yourself");
            }

            //5개 신고 새로 발행
            if(duration.toSeconds() >= 24*60*60) {
                memberRepository.modifyReportCount(memberId, 5);
            }

            //남아있는 신고 횟수가 0회
            if(member.getReportCount() == 0) {
                throw new RuntimeException("can't report anymore!");
            }
            //
            else {
                //이미 신고 한 경우
                if(privatePostReportRepository.existsByPostAndMember(privatePost, member)){
                    throw new RuntimeException("already reported");
                }
                //신고 하는 부분
                else {
                    //신고 5회 삭제
                    if(privatePost.getReportCount() == 4) {
//                        privatePostRepository.modifyReportCount(postId, privatePost.getReportCount() + 1);
                        deletePost(postId);
                        memberRepository.modifyReportCount(memberId, member.getReportCount() - 1);
                    }
                    //신고 추가
                    else {
                        privatePostRepository.modifyReportCount(postId, privatePost.getReportCount() + 1);
                        memberRepository.modifyReportCount(memberId, member.getReportCount() - 1);
                        PrivatePostReport privatePostReport = PrivatePostReport.builder().member(member).post(privatePost).build();
                        privatePostReportRepository.save(privatePostReport);
                    }
                }
            }

        }
        else {
            throw new RuntimeException("invalid Id");
        }
        return 1;
    }


    /*
     *
     * 댓글 기능
     *
     */

    @Transactional
    public Long addComment(PrivateCommentDto privateCommentDto) {
        Long memberId = privateCommentDto.getMemberId();
        Long postId = privateCommentDto.getPostId();

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<PrivatePost> optionalPrivatePost = privatePostRepository.findById(postId);

        System.out.println("isAnonymous = " + privateCommentDto.isAnonymous());

        if(optionalMember.isEmpty() || optionalPrivatePost.isEmpty()) {
            throw new RuntimeException("invalid user or post id");
        }
        else{
            PrivateComment privateComment;
            //대댓글이 아닌 그냥 댓글
            if(privateCommentDto.getParentCommentId() == null) {
                //익명 댓글의 경우
                if(privateCommentDto.isAnonymous()){
                    //해당 게시글에 멤버가 익명 댓글을 쓴 적이 있는 경우
                    if(privateCommentRepository.existsByMemberAndPostAndAnonymous(optionalMember.get(), optionalPrivatePost.get(), true) ) {
                        Pageable pageable = PageRequest.of(0, 1);
                        List<PrivateComment> privateCommentList = privateCommentRepository.findByMemberAndPostAndAnonymous(optionalMember.get(), optionalPrivatePost.get(), true, pageable);
                        Integer anonymousCount = privateCommentList.get(0).getAnonymousCount();

                        privateComment = PrivateComment.builder()
                                .post(optionalPrivatePost.get())
                                .member(optionalMember.get())
                                .content(privateCommentDto.getContent())
                                .anonymous(privateCommentDto.isAnonymous())
                                .anonymousCount(anonymousCount)
                                .likeCount(0)
                                .reportCount(0)
                                .parentComment(null)
                                .build();
                    }
                    else {
                        //익명 댓글 작성자가 게시글 작성자일 떄
                        if(Objects.equals(optionalPrivatePost.get().getMember().getId(), memberId)) {
                            privateComment = PrivateComment.builder()
                                    .post(optionalPrivatePost.get())
                                    .member(optionalMember.get())
                                    .content(privateCommentDto.getContent())
                                    .anonymous(privateCommentDto.isAnonymous())
                                    .anonymousCount(0)
                                    .likeCount(0)
                                    .reportCount(0)
                                    .parentComment(null)
                                    .build();
                        }
                        //새로운 익명 숫자 부여
                        else {
                            Integer anonymousCount = optionalPrivatePost.get().getAnonymousCount();
                            privateComment = PrivateComment.builder()
                                    .post(optionalPrivatePost.get())
                                    .member(optionalMember.get())
                                    .content(privateCommentDto.getContent())
                                    .anonymous(privateCommentDto.isAnonymous())
                                    .anonymousCount(anonymousCount)
                                    .likeCount(0)
                                    .reportCount(0)
                                    .parentComment(null)
                                    .build();

                            privatePostRepository.modifyAnonymousCount(anonymousCount + 1, postId);
                        }
                    }
                }
                //익명 댓글이 아닌 경우
                else {
                    privateComment = PrivateComment.builder()
                            .post(optionalPrivatePost.get())
                            .member(optionalMember.get())
                            .content(privateCommentDto.getContent())
                            .anonymous(privateCommentDto.isAnonymous())
                            .anonymousCount(null)
                            .likeCount(0)
                            .reportCount(0)
                            .parentComment(null)
                            .build();
                }
            }


            //대댓글인 경우
            else {
                Optional<PrivateComment> commentOptional = privateCommentRepository.findById(privateCommentDto.getParentCommentId());

                if(commentOptional.isEmpty()) {
                    throw new RuntimeException("invalid postId!!");
                }

                if(privateCommentDto.isAnonymous()){
                    //해당 게시글에 멤버가 익명 댓글을 쓴 적이 있는 경우
                    if(privateCommentRepository.existsByMemberAndPostAndAnonymous(optionalMember.get(), optionalPrivatePost.get(), true) ) {
                        Pageable pageable = PageRequest.of(0, 1);
                        List<PrivateComment> privateCommentList = privateCommentRepository.findByMemberAndPostAndAnonymous(optionalMember.get(), optionalPrivatePost.get(), true, pageable);
                        Integer anonymousCount = privateCommentList.get(0).getAnonymousCount();

                        privateComment = PrivateComment.builder()
                                .post(optionalPrivatePost.get())
                                .member(optionalMember.get())
                                .content(privateCommentDto.getContent())
                                .anonymous(privateCommentDto.isAnonymous())
                                .anonymousCount(anonymousCount)
                                .likeCount(0)
                                .reportCount(0)
                                .parentComment(commentOptional.get())
                                .build();
                    }
                    else {
                        //익명 댓글 작성자가 게시글 작성자일 떄
                        if(Objects.equals(optionalPrivatePost.get().getMember().getId(), memberId)) {
                            privateComment = PrivateComment.builder()
                                    .post(optionalPrivatePost.get())
                                    .member(optionalMember.get())
                                    .content(privateCommentDto.getContent())
                                    .anonymous(privateCommentDto.isAnonymous())
                                    .anonymousCount(0)
                                    .likeCount(0)
                                    .reportCount(0)
                                    .parentComment(commentOptional.get())
                                    .build();
                        }
                        //새로운 익명 숫자 부여
                        else {
                            Integer anonymousCount = optionalPrivatePost.get().getAnonymousCount();
                            privateComment = PrivateComment.builder()
                                    .post(optionalPrivatePost.get())
                                    .member(optionalMember.get())
                                    .content(privateCommentDto.getContent())
                                    .anonymous(privateCommentDto.isAnonymous())
                                    .anonymousCount(anonymousCount)
                                    .likeCount(0)
                                    .reportCount(0)
                                    .parentComment(commentOptional.get())
                                    .build();

                            privatePostRepository.modifyAnonymousCount(anonymousCount + 1, postId);
                        }
                    }
                }
                //익명 댓글이 아닌 경우
                else {
                    privateComment = PrivateComment.builder()
                            .post(optionalPrivatePost.get())
                            .member(optionalMember.get())
                            .content(privateCommentDto.getContent())
                            .anonymous(privateCommentDto.isAnonymous())
                            .anonymousCount(null)
                            .likeCount(0)
                            .reportCount(0)
                            .parentComment(commentOptional.get())
                            .build();
                }
            }
            Integer commentCount = optionalPrivatePost.get().getCommentCount();

            privatePostRepository.modifyCommentCount(commentCount + 1, postId);

            privateCommentRepository.save(privateComment);

            return privateComment.getId();
        }
    }

    @Transactional
    public List<PrivateCommentInfoDto> getCommentByPostAndMember(Long postId, Long memberId) {

        if(!privateCommentRepository.existsById(postId)) throw new RuntimeException("invalid postId");
        List<PrivateComment> commentNoReplies = privateCommentRepository.getParentCommentByPostAndMember(postId);

        List<PrivateCommentInfoDto> ans = commentNoReplies.stream().map(s -> {
            return PrivateCommentInfoDto.builder()
                    .id(s.getId())
                    .memberId(s.getMember().getId())
                    .memberName(s.getMember().getUsername())
                    .content(s.getContent())
                    .createdAt(s.getCreatedAt())
                    .editedAt(s.getEditedAt())
                    .deleted(s.isDeleted())
                    .anonymous(s.isAnonymous())
                    .anonymousCount(s.getAnonymousCount())
                    .likeCount(s.getLikeCount())
                    .likedByMember(privateCommentLikeRepository.existsByCommentAndMember(s, memberRepository.findById(memberId).orElseThrow(() ->new RuntimeException("invalid user!"))))
                    .reply(s.getReply().stream().map(t -> {
                        return PrivateReplyDto.builder()
                                .id(t.getId())
                                .memberId(t.getMember().getId())
                                .memberName(t.getMember().getUsername())
                                .content(t.getContent())
                                .createdAt(t.getCreatedAt())
                                .editedAt(t.getEditedAt())
                                .deleted(t.isDeleted())
                                .anonymous(t.isAnonymous())
                                .anonymousCount(t.getAnonymousCount())
                                .likeCount(t.getLikeCount())
                                .likedByMe(privateCommentLikeRepository.existsByCommentAndMember(t, memberRepository.findById(memberId).orElseThrow(() ->new RuntimeException("invalid user!"))))
                                .build();
                    }).collect(Collectors.toList()))
                    .build();
        }).collect(Collectors.toList());

        return ans;

    }

    @Transactional
    public Long getNumPostOfCommented(Long memberId) {
        return privateCommentRepository.getNumPostOfCommented(memberId);
    }

    @Transactional
    public List<PrivatePostInfoDto> getPostByMemberComment(Long memberId) {
        return privatePostRepository.getPostByMemberComment(memberId);
    }

    @Transactional
    public Integer deleteComment(Long commentId) {
        privateCommentReportRepository.deleteByCommentId(commentId);
        privateCommentLikeRepository.deleteByCommentId(commentId);
        privateCommentRepository.deleteComment(commentId);
        return 1;
    }

    @Transactional
    public Integer modifyComment(Long commentId, String content) {
        LocalDateTime now = LocalDateTime.now();
        privateCommentRepository.modifyComment(commentId, now, content);
        return 1;
    }

    /*
     * 댓글 좋야요 기능
     */

    @Transactional
    public Long addCommentLike(Long commentId, Long memberId) {
        Optional<PrivateComment> optionalPrivateComment = privateCommentRepository.findById(commentId);
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if(optionalPrivateComment.isEmpty() || optionalMember.isEmpty()) {
            throw new RuntimeException("invalid comment or member id!!");
        }
        else{
            if(memberId.equals(optionalPrivateComment.get().getMember().getId())) {
                throw new RuntimeException("can't like your own comment!");
            }
            else {
                if(privateCommentLikeRepository.existsByCommentAndMember(optionalPrivateComment.get(), optionalMember.get())) {
                    throw new RuntimeException("existing like!");
                }
                PrivateCommentLike privateCommentLike = PrivateCommentLike.builder()
                        .comment(optionalPrivateComment.get())
                        .member(optionalMember.get())
                        .build();
                privateCommentLikeRepository.save(privateCommentLike);
                privateCommentRepository.modifyLikeCount(commentId, optionalPrivateComment.get().getLikeCount() + 1);
                return privateCommentLike.getId();
            }
        }
    }

    @Transactional
    public Long deleteCommentLike(Long commentId, Long memberId) {
        Optional<PrivateComment> optionalPrivateComment = privateCommentRepository.findById(commentId);
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if(optionalPrivateComment.isEmpty() || optionalMember.isEmpty()) {
            throw new RuntimeException("invalid comment or member id!!");
        }
        else{
            if(privateCommentLikeRepository.existsByCommentAndMember(optionalPrivateComment.get(), optionalMember.get())) {
                privateCommentLikeRepository.deleteByCommentAndMember(optionalPrivateComment.get(), optionalMember.get());
                privateCommentRepository.modifyLikeCount(commentId, optionalPrivateComment.get().getLikeCount() - 1);
                return 1L;
            }
            else{
                throw new RuntimeException("non-existent like");
            }

        }
    }

    @Transactional
    public Integer getLikeByComment(Long commentId) {
        return privateCommentRepository.findById(commentId).map(PrivateComment::getLikeCount).orElseThrow(() -> new RuntimeException("invalid comment-id"));
    }

    @Transactional
    public Boolean isCommentLikedByMember(Long commentId, Long memberId) {
        Optional<PrivateComment> optionalPrivateComment = privateCommentRepository.findById(commentId);
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if(optionalPrivateComment.isEmpty() || optionalMember.isEmpty()) {
            throw new RuntimeException("invalid comment or member id!!");
        }
        else{
            return privateCommentLikeRepository.existsByCommentAndMember(optionalPrivateComment.get(), optionalMember.get());
        }
    }

    /*
     * 댓글 신고 기능
     */

    @Transactional
    public Integer reportComment(Long memberId, Long commentId) {
        if(memberRepository.existsById(memberId) && privateCommentRepository.existsById(commentId)){
            Member member = memberRepository.getById(memberId);
            PrivateComment privateComment = privateCommentRepository.getById(commentId);

            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(now, member.getReportLastIssuedAt());

            if(Objects.equals(privateComment.getMember().getId(), memberId)) {
                throw new RuntimeException("can't report yourself");
            }

            //5개 신고 새로 발행
            if(duration.toSeconds() >= 24*60*60) {
                memberRepository.modifyReportCount(memberId, 5);
            }

            //남아있는 신고 횟수가 0회
            if(member.getReportCount() == 0) {
                throw new RuntimeException("can't report anymore!");
            }
            //
            else {
                //이미 신고 한 경우
                if(privateCommentReportRepository.existsByCommentAndMember(privateComment, member)){
                    throw new RuntimeException("already reported");
                }
                //신고 하는 부분
                else {
                    //신고 5회 삭제
                    if(privateComment.getReportCount() == 4) {
//                        privatePostRepository.modifyReportCount(postId, privatePost.getReportCount() + 1);
                        deleteComment(commentId);
                        memberRepository.modifyReportCount(memberId, member.getReportCount() - 1);
                    }
                    //신고 추가
                    else {
                        privateCommentRepository.modifyReportCount(commentId, privateComment.getReportCount() + 1);
                        memberRepository.modifyReportCount(memberId, member.getReportCount() - 1);
                        PrivateCommentReport privateCommentReport = PrivateCommentReport.builder().member(member).comment(privateComment).build();
//                        privateCommentReportRepository.save(privateCommentReport);
                        privateCommentReportRepository.save(privateCommentReport);
                    }
                }
            }

        }
        else {
            throw new RuntimeException("invalid Id");
        }
        return 1;
    }


}

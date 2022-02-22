package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.PrivateCommunityDto.*;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PrivateCommunity.*;
import com.passionPay.passionPayBackEnd.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
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

//    @Transactional
//    public Long addCommunity(PrivateCommunityDto privateCommunityDto) {
//
//        if(privateCommunityRepository.existsByCommunityNameAndCommunityType(privateCommunityDto.getCommunityName(), privateCommunityDto.getCommunityType())) {
//            throw new RuntimeException("existing community!");
//        }
//        else {
//            PrivateCommunity privateCommunity = privateCommunityDto.toPrivateCommunity();
//            privateCommunityRepository.save(privateCommunity);
//            return privateCommunity.getId();
//        }
//
//    }

    /*
     * 게시글 기능
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
                .build();

        privatePostRepository.save(privatePost);
        return  privatePost.getId();
    }

    @Transactional
    public List<PrivatePostInfoDto> getPostBySchoolAndCommunity(String schoolName, PrivateCommunityType communityType, int pageSize, int pageNumber) {
        Pageable pageable = (Pageable) PageRequest.of(pageNumber, pageSize);
        return privatePostRepository.getPostBySchoolAndCommunity(schoolName, communityType, pageable);
    }

    @Transactional
    public List<PrivatePostInfoDto> getPostByMember(Long memberId, int pageSize, int pageNumber) {
        Pageable pageable = (Pageable) PageRequest.of(pageNumber, pageSize);
        return privatePostRepository.getPostByMember(memberId, pageable);
    }

    @Transactional
    public List<PrivatePostInfoDto> getPostByCommunityAndMember(PrivateCommunityType communityType, Long memberId, int pageSize, int pageNumber) {
        Pageable pageable = (Pageable) PageRequest.of(pageNumber, pageSize);
        return privatePostRepository.getPostByCommunityAndMember(communityType, memberId, pageable);
    }

    @Transactional
    public Integer getPostCountByMember(Long memberId) {
        return privatePostRepository.getPostCountByMember(memberId);
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

            if(optionalPrivatePost.get().getMember().getId() == memberId){
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
     * 댓글 기능
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
                                .parentComment(null)
                                .build();
                    }
                    else {
                        //익명 댓글 작성자가 게시글 작성자일 떄
                        if(optionalPrivatePost.get().getMember().getId() == memberId) {
                            privateComment = PrivateComment.builder()
                                    .post(optionalPrivatePost.get())
                                    .member(optionalMember.get())
                                    .content(privateCommentDto.getContent())
                                    .anonymous(privateCommentDto.isAnonymous())
                                    .anonymousCount(0)
                                    .parentComment(null)
                                    .build();
                        }
                        //새로운 익명 숫자 부여
                        else {
//                            System.out.println("new anonymous user!");
                            Integer anonymousCount = optionalPrivatePost.get().getAnonymousCount();
                            privateComment = PrivateComment.builder()
                                    .post(optionalPrivatePost.get())
                                    .member(optionalMember.get())
                                    .content(privateCommentDto.getContent())
                                    .anonymous(privateCommentDto.isAnonymous())
                                    .anonymousCount(anonymousCount)
                                    .parentComment(null)
                                    .build();

                            privatePostRepository.modifyAnonymousCount(anonymousCount + 1, postId);

//                            PrivatePost privatePost = optionalPrivatePost.get();
//                            privatePost.setAnonymousCount(anonymousCount + 1);
////                            System.out.println("new anonymous count: " + privatePost.getAnonymousCount());
//                            privatePostRepository.saveAndFlush(privatePost);
////                            System.out.println("real new anonymous count: " + privatePostRepository.findById(postId).get().getAnonymousCount());
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
                                .parentComment(commentOptional.get())
                                .build();
                    }
                    else {
                        //익명 댓글 작성자가 게시글 작성자일 떄
                        if(optionalPrivatePost.get().getMember().getId() == memberId) {
                            privateComment = PrivateComment.builder()
                                    .post(optionalPrivatePost.get())
                                    .member(optionalMember.get())
                                    .content(privateCommentDto.getContent())
                                    .anonymous(privateCommentDto.isAnonymous())
                                    .anonymousCount(0)
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
                            .parentComment(commentOptional.get())
                            .build();
                }
            }
            Integer commentCount = optionalPrivatePost.get().getCommentCount();

            privatePostRepository.modifyCommentCount(commentCount + 1, postId);

            privateCommentRepository.save(privateComment);

//            System.out.println("real new anonymous count: " + privatePostRepository.findById(postId).get().getAnonymousCount());
//            System.out.println("real new comment count: " + privatePostRepository.findById(postId).get().getCommentCount());
            return privateComment.getId();
        }
    }

    @Transactional
    public List<PrivateCommentInfoDto> getCommentByPostAndMember(Long postId, Long memberId) {

        List<PrivateComment> commentNoReplies = privateCommentRepository.findByParentComment(null);

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
                    .likeCount(privateCommentLikeRepository.getLikeByComment(s.getId()))
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
                                .likeCount(privateCommentLikeRepository.getLikeByComment(t.getId()))
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
        return privateCommentRepository
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
            if(memberId == optionalPrivateComment.get().getMember().getId()) {
                throw new RuntimeException("can't like your own comment!");
            }
            else {
                PrivateCommentLike privateCommentLike = PrivateCommentLike.builder()
                        .comment(optionalPrivateComment.get())
                        .member(optionalMember.get())
                        .build();
                privateCommentLikeRepository.save(privateCommentLike);
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
                return 1L;
            }
            else{
                throw new RuntimeException("non-existent like");
            }

        }
    }

    @Transactional
    public Integer getLikeByComment(Long commentId) {
        if(privateCommentRepository.existsById(commentId)) {
            return privateCommentLikeRepository.getLikeByComment(commentId);
        }
        else {
            throw new RuntimeException("non-existent comment");
        }
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


//    @Transactional
//    public List<PrivateComment> getMyComment(Long memberId) {
//        Optional<Member> optionalMember = memberRepository.findById(memberId);
//
//        if(optionalMember.isEmpty()) {
//            throw new RuntimeException("non-existent user!!");
//        }
//        else {
//            return privateCommentRepository.findByMember(optionalMember.get());
//        }
//    }


}

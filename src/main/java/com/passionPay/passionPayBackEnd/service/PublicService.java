package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto.*;
import com.passionPay.passionPayBackEnd.controller.dto.PublicCommunityDto.PublicCommentInfoDto;
import com.passionPay.passionPayBackEnd.domain.Member;
import com.passionPay.passionPayBackEnd.domain.PublicCommunity.*;
import com.passionPay.passionPayBackEnd.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicService {

    private final MemberRepository memberRepository;
    private final PublicPostRepository publicPostRepository;
    private final PublicPostLikeRepository publicPostLikeRepository;
    private final PublicCommentRepository publicCommentRepository;
    private final PublicCommentLikeRepository publicCommentLikeRepository;

    /*
     * 게시글 기능
     */

    @Transactional
    public Long addPost(PublicPostDto publicPostDto) {

        Optional<Member> opMember = memberRepository.findById(publicPostDto.getMemberId());

        if(opMember.isEmpty()) {
            throw new RuntimeException("Wrong user Id!");
        }

        PublicPost publicPost = PublicPost.builder()
                .content(publicPostDto.getContent())
                .title(publicPostDto.getTitle())
                .photoUrl(publicPostDto.getPhotoUrl())
                .member(opMember.get())
                .anonymous(publicPostDto.isAnonymous())
                .communityType(publicPostDto.getCommunityType())
                .anonymousCount(1)
                .commentCount(0)
                .likeCount(0)
                .reportCount(0)
                .build();

        publicPostRepository.save(publicPost);
        return  publicPost.getId();
    }

    @Transactional
    public List<PublicPostInfoDto> getPostByCommunity(PublicCommunityType communityType, int pageSize, int pageNumber) {
        Pageable pageable = (Pageable) PageRequest.of(pageNumber, pageSize);
        return publicPostRepository.getPostByCommunity(communityType, pageable);
    }

    @Transactional
    public List<PublicPostInfoDto> getPostByMember(Long memberId, int pageSize, int pageNumber) {
        Pageable pageable = (Pageable) PageRequest.of(pageNumber, pageSize);
        return publicPostRepository.getPostByMember(memberId, pageable);
    }

    @Transactional
    public List<PublicPostInfoDto> getPostByCommunityAndMember(PublicCommunityType communityType, Long memberId, int pageSize, int pageNumber) {
        Pageable pageable = (Pageable) PageRequest.of(pageNumber, pageSize);
        return publicPostRepository.getPostByCommunityAndMember(communityType, memberId, pageable);
    }

    @Transactional
    public Integer getPostCountByMember(Long memberId) {
        return publicPostRepository.getPostCountByMember(memberId);
    }

    @Transactional
    public Integer modifyPost(Long postId, PublicPostModifyDto publicPostModifyDto) {
        return publicPostRepository.findById(postId).map(publicPost -> {
            LocalDateTime editedAt = LocalDateTime.now();
            publicPostRepository.modifyPost(postId, publicPostModifyDto.getContent(), publicPostModifyDto.getTitle(), publicPostModifyDto.getPhotoUrl(), publicPostModifyDto.isAnonymous(), editedAt);
            return 1;
        }).orElseThrow(() -> new RuntimeException("invalid postId"));
    }

    @Transactional
    public Integer deletePost(Long postId) {
        return publicPostRepository.findById(postId).map(publicPost -> {

            publicPostRepository.deleteById(postId);
            publicPostLikeRepository.deleteByMemberId(postId);
            publicCommentLikeRepository.deleteByPostId(postId);
            publicCommentRepository.nullifyParentCommentByPost(postId);
            publicCommentRepository.deleteCommentByPost(postId);

            return 1;
        }).orElseThrow(() -> new RuntimeException("invalid postId"));
    }



    /*
     * 게시글 좋아요 기능
     */

    @Transactional
    public Long addPostLike(Long postId, Long memberId){
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<PublicPost> optionalPublicPost = publicPostRepository.findById(postId);

        if(optionalMember.isEmpty() || optionalPublicPost.isEmpty()) {
            throw new RuntimeException("invalid post or member Id!!");
        }
        else {

            if(publicPostLikeRepository.existsByMemberAndPost(optionalMember.get(), optionalPublicPost.get())) {
                throw new RuntimeException("already existing like!!");
            }

            if(optionalPublicPost.get().getMember().getId() == memberId){
                throw new RuntimeException("can't like your own post");
            }

            PublicPostLike publicPostLike = PublicPostLike.builder()
                    .member(optionalMember.get())
                    .post(optionalPublicPost.get())
                    .build();
            publicPostLikeRepository.save(publicPostLike);
            int likeCount = optionalPublicPost.get().getLikeCount();
            publicPostRepository.modifyLikeCount(likeCount + 1, postId);
            return publicPostLike.getId();
        }
    }

    @Transactional
    public Long deletePostLike(Long postId, Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<PublicPost> optionalPublicPost = publicPostRepository.findById(postId);

        if(optionalMember.isEmpty() || optionalPublicPost.isEmpty()) {
            throw new RuntimeException("invalid post or member Id!!");
        }
        else {

            if(publicPostLikeRepository.existsByMemberAndPost(optionalMember.get(), optionalPublicPost.get())) {
                publicPostLikeRepository.deleteByMemberAndPost(optionalMember.get(), optionalPublicPost.get());
                int likeCount = optionalPublicPost.get().getLikeCount();
                publicPostRepository.modifyLikeCount(likeCount - 1, postId);
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
        Optional<PublicPost> optionalPublicPost = publicPostRepository.findById(postId);

        if(optionalMember.isEmpty() || optionalPublicPost.isEmpty()) {
            throw new RuntimeException("invalid post or member Id!!");
        }
        else {
            return publicPostLikeRepository.existsByMemberAndPost(optionalMember.get(), optionalPublicPost.get());
        }

    }

    @Transactional
    public int likeCountOfPost(Long postId) {
        Optional<PublicPost> optionalPublicPost = publicPostRepository.findById(postId);

        if(optionalPublicPost.isEmpty()) {
            throw new RuntimeException("non-existent post!!");
        }
        else {
            return optionalPublicPost.get().getLikeCount();
        }

    }

    /*
     * 댓글 기능
     */

    @Transactional
    public Long addComment(PublicCommentDto publicCommentDto) {
        Long memberId = publicCommentDto.getMemberId();
        Long postId = publicCommentDto.getPostId();

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<PublicPost> optionalPublicPost = publicPostRepository.findById(postId);

        System.out.println("isAnonymous = " + publicCommentDto.isAnonymous());

        if(optionalMember.isEmpty() || optionalPublicPost.isEmpty()) {
            throw new RuntimeException("invalid user or post id");
        }
        else{
            PublicComment publicComment;
            //대댓글이 아닌 그냥 댓글
            if(publicCommentDto.getParentCommentId() == null) {
                //익명 댓글의 경우
                if(publicCommentDto.isAnonymous()){
                    //해당 게시글에 멤버가 익명 댓글을 쓴 적이 있는 경우
                    if(publicCommentRepository.existsByMemberAndPostAndAnonymous(optionalMember.get(), optionalPublicPost.get(), true) ) {
                        Pageable pageable = PageRequest.of(0, 1);
                        List<PublicComment> publicCommentList = publicCommentRepository.findByMemberAndPostAndAnonymous(optionalMember.get(), optionalPublicPost.get(), true, pageable);
                        Integer anonymousCount = publicCommentList.get(0).getAnonymousCount();

                        publicComment = PublicComment.builder()
                                .post(optionalPublicPost.get())
                                .member(optionalMember.get())
                                .content(publicCommentDto.getContent())
                                .anonymous(publicCommentDto.isAnonymous())
                                .anonymousCount(anonymousCount)
                                .likeCount(0)
                                .reportCount(0)
                                .parentComment(null)
                                .build();
                    }
                    else {
                        //익명 댓글 작성자가 게시글 작성자일 떄
                        if(optionalPublicPost.get().getMember().getId() == memberId) {
                            publicComment = PublicComment.builder()
                                    .post(optionalPublicPost.get())
                                    .member(optionalMember.get())
                                    .content(publicCommentDto.getContent())
                                    .anonymous(publicCommentDto.isAnonymous())
                                    .anonymousCount(0)
                                    .likeCount(0)
                                    .reportCount(0)
                                    .parentComment(null)
                                    .build();
                        }
                        //새로운 익명 숫자 부여
                        else {
                            Integer anonymousCount = optionalPublicPost.get().getAnonymousCount();
                            publicComment = PublicComment.builder()
                                    .post(optionalPublicPost.get())
                                    .member(optionalMember.get())
                                    .content(publicCommentDto.getContent())
                                    .anonymous(publicCommentDto.isAnonymous())
                                    .anonymousCount(anonymousCount)
                                    .likeCount(0)
                                    .reportCount(0)
                                    .parentComment(null)
                                    .build();

                            publicPostRepository.modifyAnonymousCount(anonymousCount + 1, postId);
                        }
                    }
                }
                //익명 댓글이 아닌 경우
                else {
                    publicComment = PublicComment.builder()
                            .post(optionalPublicPost.get())
                            .member(optionalMember.get())
                            .content(publicCommentDto.getContent())
                            .anonymous(publicCommentDto.isAnonymous())
                            .anonymousCount(null)
                            .likeCount(0)
                            .reportCount(0)
                            .parentComment(null)
                            .build();
                }
            }


            //대댓글인 경우
            else {
                Optional<PublicComment> commentOptional = publicCommentRepository.findById(publicCommentDto.getParentCommentId());

                if(commentOptional.isEmpty()) {
                    throw new RuntimeException("invalid postId!!");
                }

                if(publicCommentDto.isAnonymous()){
                    //해당 게시글에 멤버가 익명 댓글을 쓴 적이 있는 경우
                    if(publicCommentRepository.existsByMemberAndPostAndAnonymous(optionalMember.get(), optionalPublicPost.get(), true) ) {
                        Pageable pageable = PageRequest.of(0, 1);
                        List<PublicComment> publicCommentList = publicCommentRepository.findByMemberAndPostAndAnonymous(optionalMember.get(), optionalPublicPost.get(), true, pageable);
                        Integer anonymousCount = publicCommentList.get(0).getAnonymousCount();

                        publicComment = PublicComment.builder()
                                .post(optionalPublicPost.get())
                                .member(optionalMember.get())
                                .content(publicCommentDto.getContent())
                                .anonymous(publicCommentDto.isAnonymous())
                                .anonymousCount(anonymousCount)
                                .likeCount(0)
                                .reportCount(0)
                                .parentComment(commentOptional.get())
                                .build();
                    }
                    else {
                        //익명 댓글 작성자가 게시글 작성자일 떄
                        if(optionalPublicPost.get().getMember().getId() == memberId) {
                            publicComment = PublicComment.builder()
                                    .post(optionalPublicPost.get())
                                    .member(optionalMember.get())
                                    .content(publicCommentDto.getContent())
                                    .anonymous(publicCommentDto.isAnonymous())
                                    .anonymousCount(0)
                                    .likeCount(0)
                                    .reportCount(0)
                                    .parentComment(commentOptional.get())
                                    .build();
                        }
                        //새로운 익명 숫자 부여
                        else {
                            Integer anonymousCount = optionalPublicPost.get().getAnonymousCount();
                            publicComment = PublicComment.builder()
                                    .post(optionalPublicPost.get())
                                    .member(optionalMember.get())
                                    .content(publicCommentDto.getContent())
                                    .anonymous(publicCommentDto.isAnonymous())
                                    .anonymousCount(anonymousCount)
                                    .likeCount(0)
                                    .reportCount(0)
                                    .parentComment(commentOptional.get())
                                    .build();

                            publicPostRepository.modifyAnonymousCount(anonymousCount + 1, postId);
                        }
                    }
                }
                //익명 댓글이 아닌 경우
                else {
                    publicComment = PublicComment.builder()
                            .post(optionalPublicPost.get())
                            .member(optionalMember.get())
                            .content(publicCommentDto.getContent())
                            .anonymous(publicCommentDto.isAnonymous())
                            .anonymousCount(null)
                            .likeCount(0)
                            .reportCount(0)
                            .parentComment(commentOptional.get())
                            .build();
                }
            }
            Integer commentCount = optionalPublicPost.get().getCommentCount();

            publicPostRepository.modifyCommentCount(commentCount + 1, postId);

            publicCommentRepository.save(publicComment);

//            System.out.println("real new anonymous count: " + publicPostRepository.findById(postId).get().getAnonymousCount());
//            System.out.println("real new comment count: " + publicPostRepository.findById(postId).get().getCommentCount());
            return publicComment.getId();
        }
    }

    @Transactional
    public List<PublicCommentInfoDto> getCommentByPostAndMember(Long postId, Long memberId) {

        if(!publicCommentRepository.existsById(postId)) throw new RuntimeException("invalid postId");
        List<PublicComment> commentNoReplies = publicCommentRepository.getParentCommentByPostAndMember(postId);

        List<PublicCommentInfoDto> ans = commentNoReplies.stream().map(s -> {
            return PublicCommentInfoDto.builder()
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
                    .likedByMember(publicCommentLikeRepository.existsByCommentAndMember(s, memberRepository.findById(memberId).orElseThrow(() ->new RuntimeException("invalid user!"))))
                    .reply(s.getReply().stream().map(t -> {
                        return PublicReplyDto.builder()
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
                                .likedByMe(publicCommentLikeRepository.existsByCommentAndMember(t, memberRepository.findById(memberId).orElseThrow(() ->new RuntimeException("invalid user!"))))
                                .build();
                    }).collect(Collectors.toList()))
                    .build();
        }).collect(Collectors.toList());

        return ans;

    }

    @Transactional
    public Long getNumPostOfCommented(Long memberId) {
        return publicCommentRepository.getNumPostOfCommented(memberId);
    }

    @Transactional
    public List<PublicPostInfoDto> getPostByMemberComment(Long memberId) {
        return publicPostRepository.getPostByMemberComment(memberId);
    }

    @Transactional
    public Integer deleteComment(Long commentId) {
        publicCommentRepository.deleteComment(commentId);
        return 1;
    }

    @Transactional
    public Integer modifyComment(Long commentId, String content) {
        LocalDateTime now = LocalDateTime.now();
        publicCommentRepository.modifyComment(commentId, now, content);
        return 1;
    }

    /*
     * 댓글 좋야요 기능
     */

    @Transactional
    public Long addCommentLike(Long commentId, Long memberId) {
        Optional<PublicComment> optionalPublicComment = publicCommentRepository.findById(commentId);
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if(optionalPublicComment.isEmpty() || optionalMember.isEmpty()) {
            throw new RuntimeException("invalid comment or member id!!");
        }
        else{
            if(memberId == optionalPublicComment.get().getMember().getId()) {
                throw new RuntimeException("can't like your own comment!");
            }
            else {
                if(publicCommentLikeRepository.existsByCommentAndMember(optionalPublicComment.get(), optionalMember.get())) {
                    throw new RuntimeException("existing like!");
                }
                PublicCommentLike publicCommentLike = PublicCommentLike.builder()
                        .comment(optionalPublicComment.get())
                        .member(optionalMember.get())
                        .build();
                publicCommentLikeRepository.save(publicCommentLike);
                publicCommentRepository.modifyLikeCount(commentId, optionalPublicComment.get().getLikeCount() + 1);
                return publicCommentLike.getId();
            }
        }
    }

    @Transactional
    public Long deleteCommentLike(Long commentId, Long memberId) {
        Optional<PublicComment> optionalPublicComment = publicCommentRepository.findById(commentId);
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if(optionalPublicComment.isEmpty() || optionalMember.isEmpty()) {
            throw new RuntimeException("invalid comment or member id!!");
        }
        else{
            if(publicCommentLikeRepository.existsByCommentAndMember(optionalPublicComment.get(), optionalMember.get())) {
                publicCommentLikeRepository.deleteByCommentAndMember(optionalPublicComment.get(), optionalMember.get());
                publicCommentRepository.modifyLikeCount(commentId, optionalPublicComment.get().getLikeCount() - 1);
                return 1L;
            }
            else{
                throw new RuntimeException("non-existent like");
            }

        }
    }

    @Transactional
    public Integer getLikeByComment(Long commentId) {
        return publicCommentRepository.findById(commentId).map(PublicComment::getLikeCount).orElseThrow(() -> new RuntimeException("invalid comment-id"));
    }

    @Transactional
    public Boolean isCommentLikedByMember(Long commentId, Long memberId) {
        Optional<PublicComment> optionalPublicComment = publicCommentRepository.findById(commentId);
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if(optionalPublicComment.isEmpty() || optionalMember.isEmpty()) {
            throw new RuntimeException("invalid comment or member id!!");
        }
        else{
            return publicCommentLikeRepository.existsByCommentAndMember(optionalPublicComment.get(), optionalMember.get());
        }
    }

}

package com.passionPay.passionPayBackEnd.domain.PublicCommunity;

import com.passionPay.passionPayBackEnd.domain.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PublicCommentLike")
public class PublicCommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private PublicComment comment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}

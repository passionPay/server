package com.passionPay.passionPayBackEnd.domain.PrivateCommunity;

import com.passionPay.passionPayBackEnd.domain.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PrivateCommentLike")
public class PrivateCommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private PrivateComment comment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}

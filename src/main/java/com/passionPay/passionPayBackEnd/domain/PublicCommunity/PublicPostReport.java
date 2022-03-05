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
@Table(name = "PublicPostReport")
public class PublicPostReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "post_id")
    private PublicPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "member_id")
    private Member member;

}

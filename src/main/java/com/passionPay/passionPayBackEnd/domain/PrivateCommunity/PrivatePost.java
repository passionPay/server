package com.passionPay.passionPayBackEnd.domain.PrivateCommunity;

import com.passionPay.passionPayBackEnd.domain.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PrivatePost")
public class PrivatePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String photoUrl;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @CreationTimestamp
    private LocalDateTime editedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "member_id")
    private Member member;

    @Column
    private String schoolName;

    @Column
    private boolean anonymous;

    @Enumerated(EnumType.STRING)
    private PrivateCommunityType communityType;

    @Column
    private Integer anonymousCount;

    @Column
    private Integer commentCount;

    @Column
    private Integer likeCount;

    @Column
    private Integer reportCount;

}

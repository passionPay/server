package com.passionPay.passionPayBackEnd.domain.PublicCommunity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.passionPay.passionPayBackEnd.domain.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PublicComment")
public class PublicComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "post_id")
    private PublicPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "member_id")
    private Member member;

    @Column
    private String content;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @CreationTimestamp
    private LocalDateTime editedAt;

    @Column
    @ColumnDefault("false")
    private boolean deleted;

    @Column
    private boolean anonymous;

    @Column(nullable = true)
    private Integer anonymousCount;

    @Column
    private Integer likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "parent_id")
    @JsonIgnoreProperties("reply")
    private PublicComment parentComment;

    @OneToMany(mappedBy = "parentComment")
    @JsonIgnoreProperties("parentComment")
    private List<PublicComment> reply = new ArrayList<PublicComment>();

}

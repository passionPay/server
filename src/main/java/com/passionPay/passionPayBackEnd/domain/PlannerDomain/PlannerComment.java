package com.passionPay.passionPayBackEnd.domain.PlannerDomain;

import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMember;
import com.passionPay.passionPayBackEnd.domain.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="PlannerComment")
@Table(name="planner_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannerComment {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="planner_id")
    private Planner planner;

    @Column(name="content")
    private String content;

    // 댓글 작성자
    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}

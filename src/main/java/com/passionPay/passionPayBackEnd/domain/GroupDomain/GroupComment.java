package com.passionPay.passionPayBackEnd.domain.GroupDomain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="GroupComment")
@Table(name="group_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupComment {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupCommentId;

    @ManyToOne
    @JoinColumn(name="post_id")
    private GroupPost groupPost;

    // 댓글 작성자
    @ManyToOne
    @JoinColumn(name="member_id")
    private GroupMember groupMember;

    @Column(name="content")
    private String content;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}

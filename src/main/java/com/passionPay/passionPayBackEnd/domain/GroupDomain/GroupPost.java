package com.passionPay.passionPayBackEnd.domain.GroupDomain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="GroupPost")
@Table(name="group_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
// 인증 게시판
public class GroupPost {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupPostId;

    @ManyToOne
    private GroupMember groupMember; // 게시글 작성자

    @Column(name="content")
    private String content;

    @Column(name="photo_url")
    private String photoUrl;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}

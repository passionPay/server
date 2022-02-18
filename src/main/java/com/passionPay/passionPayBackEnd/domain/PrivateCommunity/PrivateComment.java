//package com.passionPay.passionPayBackEnd.domain.PrivateCommunity;
//
//import com.passionPay.passionPayBackEnd.domain.Member;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(name = "PrivateComment")
//public class PrivateComment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn( name = "post_id")
//    private PrivatePost post;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn( name = "member_id")
//    private Member member;
//
//    @Column
//    private String content;
//
//    @Column(updatable = false, nullable = false)
//    @CreationTimestamp
//    private LocalDateTime createdAt;
//
//    @Column
//    @CreationTimestamp
//    private LocalDateTime editedAt;
//
//    @Column
//    private boolean isDeleted;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn( name = "parent_id")
//    private PrivateComment parentComment;
//
//    @OneToMany(mappedBy = "parentComment")
//    private List<PrivateComment> reply = new ArrayList<PrivateComment>();
//
//}

package com.passionPay.passionPayBackEnd.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique=true)
    private String userName;

    @Column(nullable = false)
    private String passWord;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private boolean activated;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    private String photoUrl;

    @Column
    private String categoryName;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    @Column
    private int grade;



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;

//    public Member shallowCopy() {
//        Member newMember = new Member();
//
//    }

//    @Builder
//    public Member(Long id, String userName, String passWord, String email, String displayName, boolean activated, LocalDateTime createdAt, String photoUrl, String categoryName, Stage stage, int grade, Authority authority) {
//        this.id = id;
//        this.userName = userName;
//        this.passWord = passWord;
//        this.email = email;
//        this.displayName = displayName;
//        this.activated = activated;
//        this.createdAt = createdAt;
//        this.photoUrl = photoUrl;
//        this.categoryName = categoryName;
//        this.stage = stage;
//        this.grade = grade;
//        this.authority = authority;
//    }
    //    public Member(String userName, String passWord, Authority authority) {
//        this.userName = userName;
//        this.passWord = passWord;
//        this.authority = authority;
//    }

//    @PrePersist
//    public void createdAt() {
//        this.createdAt = LocalDateTime.now();
//    }
}
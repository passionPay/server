package com.passionPay.passionPayBackEnd.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

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
    private String username;

    @Column(nullable = false)
    private String password;

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
    private String schoolName;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    @Column
    private int grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;

    @Column
    private int reportCount;

    @Column
    private boolean personal;

    @Column(updatable = true, nullable = false)
    @CreationTimestamp
    private LocalDateTime reportLastIssuedAt;

}
package com.passionPay.passionPayBackEnd.domain.GroupDomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.passionPay.passionPayBackEnd.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @OneToMany(mappedBy="group")
    @Column(name="group_member")
    private List<GroupMember> groupMembers = new ArrayList<>();

    @Column(name="group_name")
    private String groupName;

    @Column(name="group_goal")
    private String groupGoal;

    @Column(name="group_time_goal")
    private int groupTimeGoal;

    @Column(name="group_password")
    private String groupPassword;

    @Column(name="max_member")
    private int maxMember;
}

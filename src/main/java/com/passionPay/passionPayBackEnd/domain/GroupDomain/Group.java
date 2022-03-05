package com.passionPay.passionPayBackEnd.domain.GroupDomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.passionPay.passionPayBackEnd.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="Group")
@Table(name="study_group", uniqueConstraints = @UniqueConstraint(columnNames = {"group_name"}))
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

    @OneToMany(mappedBy="group", cascade = CascadeType.REMOVE)
    @Column(name="group_member")
    @Builder.Default
    @JsonIgnore
    private List<GroupMember> groupMembers = new ArrayList<>();

    @Column(name="group_name")
    private String groupName;

    @Column(name="group_description")
    private String groupDescription;

    @Column(name="group_time_goal")
    @Builder.Default
    private int groupTimeGoal = 0;

    @Column(name="group_password")
    private String groupPassword;

    @Column(name="group_privacy")
    private boolean groupPrivacy;

    @Column(name="max_member")
    private int maxMember;

    public List<GroupMember> addGroupMember(GroupMember groupMember) {
        this.groupMembers.add(groupMember);
        return this.getGroupMembers();
    }

    public List<GroupMember> deleteGroupMember(GroupMember groupMember) {
        this.groupMembers.remove(groupMember);
        return this.getGroupMembers();
    }
}

package com.passionPay.passionPayBackEnd.domain.GroupDomain;

import lombok.*;

import javax.persistence.*;

@Entity(name="GroupProgress")
@Table(name="group_progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GroupProgress {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupProgressId;

    @ManyToOne
    @JoinColumn(name="group_mission")
    private GroupMission groupMission;

    @Column(name="group_member_id", insertable = false, updatable = false)
    private Long groupMemberId;

    @ManyToOne
    @JoinColumn(name="group_member")
    private GroupMember groupMember;

    @Column(name="is_complete")
    private boolean complete;
}

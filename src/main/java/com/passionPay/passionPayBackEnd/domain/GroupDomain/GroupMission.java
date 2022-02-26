package com.passionPay.passionPayBackEnd.domain.GroupDomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

/* TODO: UNIQUE CONSTRAINTS 추가해야 함 */
@Entity(name="GroupMission")
@Table(name="group_mission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMission {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupMissionId;

    @Column(name="goal_name")
    private String missionName;

    @JsonIgnore
    @ManyToOne
    private Group group;
}

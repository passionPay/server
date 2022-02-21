package com.passionPay.passionPayBackEnd.domain.PlannerDomain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;

@Entity(name="TotalTime")
@Table(name="total_time")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalTime {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long totalTimeId;

    @Column(name="planner_id")
    private Long plannerId;

    @Column(name="value")
    private LocalTime value;

    @Column(name="start_time")
    private LocalTime startTime;
}

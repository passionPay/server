package com.passionPay.passionPayBackEnd.domain.PlannerDomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;

@Entity(name="Timestamp")
@Table(name="time_stamp", uniqueConstraints = @UniqueConstraint(columnNames = {"start_time"}))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Timestamp {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timestampId;

    @JsonIgnore
    @ManyToOne
    private Task task;

    @Column(name="start_time", nullable = false)
    private LocalTime startTime;

    @Column(name="end_time", nullable = false)
    private LocalTime endTime;
}

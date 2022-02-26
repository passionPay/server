package com.passionPay.passionPayBackEnd.domain.PlannerDomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="Task")
@Table(name="task")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @JsonIgnore
    @OneToMany(cascade=CascadeType.REMOVE)
    @Column(name="timestamps")
    private List<Timestamp> timestamps = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="planner_id")
    private Planner planner;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="subject_id")
    private Subject subject;

    @Column(name="task_name")
    private String taskName;

    @ColumnDefault("0")
    @Column(name="total_time")
    private long totalTime; // ms

    @ColumnDefault("0")
    @Column(name="status")
    // 0 - 미완 / 1 - X / 2 - 세모 / 3 - O
    private int status;

    @Column(name="color")
    private String color;

    public void addTimestamp(Timestamp timestamp) {
        timestamps.add(timestamp);
    }
}

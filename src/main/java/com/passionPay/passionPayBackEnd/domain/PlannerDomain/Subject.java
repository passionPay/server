package com.passionPay.passionPayBackEnd.domain.PlannerDomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Task;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="Subject")
@Table(name="subject", uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "title"}))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    @JsonIgnore
    @Column(name="member_id")
    private Long memberId;

    @Column(name="title")
    private String title;

    @JsonIgnore
    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="subject")
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
        task.setSubject(this);
    }
}

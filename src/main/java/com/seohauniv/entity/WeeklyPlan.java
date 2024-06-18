package com.seohauniv.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "weekly_plan")
@Getter
@Setter
public class WeeklyPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekly_plan_id")
    private Long id;

    private int week;

    @Column(columnDefinition = "longtext")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "syllabus_id")
    @JsonIgnore
    private Syllabus syllabus;

    public WeeklyPlan() {
    }

    public WeeklyPlan(int week, String content, Syllabus syllabus) {
        this.week = week;
        this.content = content;
        this.syllabus = syllabus;
    }

    @Override
    public String toString() {
        return "WeeklyPlan{" +
                "id=" + id +
                ", week=" + week +
                ", content='" + content + '\'' +
                '}';
    }
}

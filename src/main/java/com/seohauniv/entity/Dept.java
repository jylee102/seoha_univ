package com.seohauniv.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "subject")
@Data
public class Dept {
    @Id
    @Column(name = "subject_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
}

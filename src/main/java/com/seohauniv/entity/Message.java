package com.seohauniv.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "message")
@Getter
@Setter
@ToString
public class Message extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_to")
    private Member sendTo; // 받는 사람

    private String title; // 미리보기 내용
    private String content; // 내용
}

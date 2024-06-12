package com.seohauniv.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Member sendTo; // 받는 사람

    private String title;

    @Column(columnDefinition = "longtext")
    private String content; // 내용

    private String isRead = "f"; // 읽었는지

    public Message() {
    }

    public Message(Member sendTo, String title, String content) {
        this.sendTo = sendTo;
        this.title = title;
        this.content = content;
    }

    public Message(Course course) {
        this.sendTo = course.getProfessor().getMember();
        this.title = "강의계획서 승인";

        this.content = course.getProfessor().getName() + " 님이 등록하신 [" +
                course.getSyllabus().getCourseName() + "] 강의 계획서가 승인되었습니다.\n" +
                "해당 강의의 과목코드는 [" + course.getId() + "]입니다.";
    }
}

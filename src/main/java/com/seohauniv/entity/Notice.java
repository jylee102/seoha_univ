package com.seohauniv.entity;

import com.seohauniv.dto.NoticeFormDto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Data
public class Notice extends BaseEntity {
    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int views;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Staff staff;

    public void updateNotice(NoticeFormDto noticeFormDto){
        this.title = noticeFormDto.getTitle();
        this.content = noticeFormDto.getContent();
    }
    public void updateViews(){this.views ++;}
}

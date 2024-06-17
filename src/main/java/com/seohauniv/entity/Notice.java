package com.seohauniv.entity;

import com.seohauniv.dto.NoticeFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "notice")
@Getter
@Setter
@ToString
public class Notice extends BaseEntity {
    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "longtext")
    private String content;

    @Column(nullable = false)
    private int views;

    public void updateNotice(NoticeFormDto noticeFormDto){
        this.title = noticeFormDto.getTitle();
        this.content = noticeFormDto.getContent();
    }
    public void updateViews(){this.views ++;}
}

package com.seohauniv.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainNoticeDto {
    private Long id;
    private String title;
    private String content;
    private int view;

    @QueryProjection
    public MainNoticeDto(Long id, String title, String content, int view){
        this.id = id;
        this.title = title;
        this.view = view;
        this.content = content;
    }
}

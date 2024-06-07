package com.seohauniv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeSearchDto {
    private String searchDateType;
    private String searchBy;
    private String searchQuery = "";

    public NoticeSearchDto() {
    }

    public NoticeSearchDto(String searchDateType, String searchBy, String searchQuery) {
        this.searchDateType = searchDateType;
        this.searchBy = searchBy;
        this.searchQuery = searchQuery;
    }
}

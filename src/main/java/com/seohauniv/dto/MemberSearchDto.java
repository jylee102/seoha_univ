package com.seohauniv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchDto {
    private String searchBy = "id";
    private String searchQuery = "";
    private String tabValue = "STUDENT";
}

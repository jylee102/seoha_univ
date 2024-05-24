package com.seohauniv.constant;

public enum AcademicStatus {
    ATTEND("재학"),
    LEAVE("휴학"),
    GRADUATE("졸업"),
    OUT("자퇴");

    private final String description;

    AcademicStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

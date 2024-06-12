package com.seohauniv.constant;

public enum AttendStatus {
    PRESENT("출석"),
    LATE("지각"),
    ABSENT("결석");

    private final String description;

    AttendStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

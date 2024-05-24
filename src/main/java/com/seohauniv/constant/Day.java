package com.seohauniv.constant;

public enum Day {
    MON("월"),
    TUE("화"),
    WED("수"),
    THUR("목"),
    FRI("금");

    private final String description;

    Day(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

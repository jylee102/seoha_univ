package com.seohauniv.constant;

public enum LeaveReason {
    OTHER("일반"),
    PARENTAL("임신·출산·육아"),
    MEDICAL("질병"),
    ENTREPRENEURSHIP("창업"),
    MILITARY("군입대");

    private final String description;

    LeaveReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

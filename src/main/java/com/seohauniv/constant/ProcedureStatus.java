package com.seohauniv.constant;

public enum ProcedureStatus {
    PROCESSING("처리중"),
    APPROVAL("승인"),
    REFUSAL("거부");

    private final String description;

    ProcedureStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

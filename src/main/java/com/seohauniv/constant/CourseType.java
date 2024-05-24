package com.seohauniv.constant;

public enum CourseType {
    MAJOR("전공"),
    LIBERAL("교양");

    private final String description;

    CourseType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

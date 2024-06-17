package com.seohauniv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgressUpdate {
    private int percentComplete;
    private String message;

    public ProgressUpdate(int percentComplete, String message) {
        this.percentComplete = percentComplete;
        this.message = message;
    }
}

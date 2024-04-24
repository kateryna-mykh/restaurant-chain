package com.katerynamykh.taskprofitsoft.backend.exception;

public class SaveDuplicateException extends RuntimeException {
    public SaveDuplicateException(String message) {
        super(message);
    }
}

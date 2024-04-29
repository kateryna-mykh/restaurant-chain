package com.katerynamykh.taskprofitsoft.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SaveDuplicateException extends RuntimeException {
    public SaveDuplicateException(String message) {
        super(message);
    }
}

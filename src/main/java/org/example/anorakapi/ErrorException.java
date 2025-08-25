package org.example.anorakapi;

import org.springframework.http.HttpStatus;
import java.util.List;

public class ErrorException extends RuntimeException {
    private final String code;
    private final List<String> errors;
    private final HttpStatus status;

    public ErrorException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.errors = null;
        this.status = status;
    }

    public ErrorException(String code, String message, List<String> errors, HttpStatus status) {
        super(message);
        this.code = code;
        this.errors = errors;
        this.status = status;
    }

    public String getCode() { return code; }
    public List<String> getErrors() { return errors; }
    public HttpStatus getStatus() { return status; }
}

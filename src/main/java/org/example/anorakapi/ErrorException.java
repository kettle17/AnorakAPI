package org.example.anorakapi;

import java.util.List;

public class ErrorException extends RuntimeException {
    private final String code;
    private final List<String> errors;

    public ErrorException(String code, String message) {
        super(message);
        this.code = code;
        this.errors = null;
    }

    public ErrorException(String code, String message, List<String> errors) {
        super(message);
        this.code = code;
        this.errors = errors;
    }

    public String getCode() { return code; }
    public List<String> getErrors() { return errors; }
}
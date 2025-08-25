package org.example.anorakapi;

import java.util.List;

public class Error {
    private String code;
    private String message;
    private List<String> errors;

    Error(String code, String message) throws IllegalArgumentException {
        validateCodeAndMessage(code, message);
        this.code = code;
        this.message = message;
        this.errors = null;
    }

    Error(String code, String message, List<String> errors) throws IllegalArgumentException {
        validateCodeAndMessage(code, message);
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public Error() {

    }

    private void validateCodeAndMessage(String code, String message) throws IllegalArgumentException{
        if (code == null || code.isEmpty() || message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Code or message cannot be null or empty");
        }
    }

    public String getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public List<String> getErrors() {
        return errors;
    }
}

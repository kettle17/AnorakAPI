package org.example.anorakapi;

import java.util.List;
import java.util.Objects;

public class Error {
    private String code;
    private String message;
    private List<Error> errors;

    Error(String code, String message) throws IllegalArgumentException {
        validateCodeAndMessage(code, message);
        this.code = code;
        this.message = message;
        this.errors = null;
    }

    Error(String code, String message, List<Error> errors) throws IllegalArgumentException {
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

    public String getErrorCode() {
        return code;
    }
    public String getErrorMessage() {
        return message;
    }
    public List<Error> getErrors() {
        return errors;
    }
}

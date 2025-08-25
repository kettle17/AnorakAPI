package org.example.anorakapi;

import java.lang.reflect.Array;

public class Error {
    private String code;
    private String message;
    private Array errors;

    Error(String code, String message, Array errors) throws IllegalArgumentException {
        if (code == null || message == null) {
            throw new IllegalArgumentException("Code or message cannot be null");
        }
        this.code = code;
        this.message = message;
        this.errors = errors;
    }


    public Error() {

    }

    public String getErrorCode() {
        return code;
    }
    public String getErrorMessage() {
        return message;
    }
    public Array getErrors() {
        return errors;
    }
}

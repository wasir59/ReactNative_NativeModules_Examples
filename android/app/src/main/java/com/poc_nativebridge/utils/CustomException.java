package com.poc_nativebridge.utils;

/**
 * Created by Mr. Wasir on 15,September,2018
 */
public class CustomException extends Exception {
    private String errorMessage;

    public CustomException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public CustomException(String message, String errorMessage) {
        super(message);
        this.errorMessage = errorMessage;
    }

    public CustomException(String message, Throwable cause, String errorMessage) {
        super(message, cause);
        this.errorMessage = errorMessage;
    }

    public CustomException(Throwable cause, String errorMessage) {
        super(cause);
        this.errorMessage = errorMessage;
    }

    public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String errorMessage) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorMessage = errorMessage;
    }
}

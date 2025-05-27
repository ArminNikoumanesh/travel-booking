package com.armin.utility.object;

import lombok.Getter;

import jakarta.servlet.http.HttpServletResponse;

@Getter
public class ErrorResult {
    private SystemError code;
    private Integer status;
    private Integer errorCode;
    private Object data;

    public ErrorResult(SystemError error, Integer errorCode, Object data) {
        this.code = error;
        this.errorCode = errorCode;
        this.data = data;
        this.status = error.getValue();
    }

    public ErrorResult(SystemException exception) {
        this.code = exception.getError();
        this.errorCode = exception.getErrorCode();
        this.data = exception.getArgument();
        this.status = exception.getError().getValue();
    }

    public ErrorResult(SystemException exception, HttpServletResponse response) {
        this.code = exception.getError();
        this.status = exception.getError().getValue();
        this.errorCode = exception.getErrorCode();
        response.setStatus(exception.getError().getValue());
    }

}

package com.armin.utility.object;

import lombok.Getter;

import java.util.List;

/**
 * System Exception Class,
 * A Customized {@link RuntimeException} for Whole System with a proper {@link SystemError} Type and Error Code
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Getter
public class SystemException extends Exception {
    private final SystemError error;
    private final Integer errorCode;
    private final Object argument;
    private final List<ErrorResult> errorResults;

    public SystemException(SystemError error, Object argument, Integer errorCode) {
        this.error = error;
        this.argument = argument;
        this.errorCode = errorCode;
        this.errorResults = null;
    }

    public SystemException(SystemError error, Object argument, Integer errorCode, List<ErrorResult> errorResults) {
        this.error = error;
        this.argument = argument;
        this.errorCode = errorCode;
        this.errorResults = errorResults;
    }

}

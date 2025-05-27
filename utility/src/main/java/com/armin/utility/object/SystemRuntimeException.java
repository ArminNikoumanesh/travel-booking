package com.armin.utility.object;

import com.armin.utility.object.SystemError;

public class SystemRuntimeException extends RuntimeException {
    private final SystemError error;
    private final Object argument;

    public SystemRuntimeException(SystemError error, Object argument) {
        this.error = error;
        this.argument = argument;
    }

    public SystemError getError() {
        return error;
    }

    public Object getArgument() {
        return argument;
    }
}

package com.w00tmast3r.skquery.util;

public class SkQueryInternalException extends RuntimeException {

    public SkQueryInternalException() {
        super();
    }

    public SkQueryInternalException(String message) {
        super(message);
    }

    public SkQueryInternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public SkQueryInternalException(Throwable cause) {
        super(cause);
    }
}

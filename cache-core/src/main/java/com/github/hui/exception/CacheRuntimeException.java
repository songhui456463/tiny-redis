package com.github.hui.exception;

import com.github.hui.core.Cache;

public class CacheRuntimeException extends RuntimeException {

    public CacheRuntimeException() {
    }

    public CacheRuntimeException(String message) {
        super(message);
    }

    public CacheRuntimeException(Throwable cause) {
        super(cause);
    }

    public CacheRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

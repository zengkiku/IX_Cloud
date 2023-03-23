package com.ix.framework.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Zeng canwei
 * @version 1.8
 * @date 2023/3/23 0023
 */
public class IXAuthenticationException extends AuthenticationException {
    public IXAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public IXAuthenticationException(String msg) {
        super(msg);
    }
}

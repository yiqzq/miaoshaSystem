package com.yiqzq.miaoshasystem.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author yiqzq
 * @date 2020/6/4 23:09
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
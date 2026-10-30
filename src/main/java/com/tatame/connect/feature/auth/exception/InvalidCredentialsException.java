package com.tatame.connect.feature.auth.exception;

import com.tatame.connect.exception.base.UnauthorizedException;

public class InvalidCredentialsException extends UnauthorizedException {

    public InvalidCredentialsException() {
        super("Email ou senha invalidos");
    }
}

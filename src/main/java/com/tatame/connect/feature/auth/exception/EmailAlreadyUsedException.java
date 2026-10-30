package com.tatame.connect.feature.auth.exception;

import com.tatame.connect.exception.base.ConflictException;

public class EmailAlreadyUsedException extends ConflictException {

    public EmailAlreadyUsedException(String email) {
        super("Ja existe uma conta cadastrada com o email " + email);
    }
}

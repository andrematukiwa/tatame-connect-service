package com.tatame.connect.feature.professor.exception;

import com.tatame.connect.exception.base.NotFoundException;
import java.util.UUID;

public class ProfessorNotFoundException extends NotFoundException {

    public ProfessorNotFoundException(UUID professorId) {
        super("Professor nao encontrado: " + professorId);
    }

    public ProfessorNotFoundException(String message) {
        super(message);
    }
}

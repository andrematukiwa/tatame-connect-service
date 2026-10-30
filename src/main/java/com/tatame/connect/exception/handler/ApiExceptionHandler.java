package com.tatame.connect.exception.handler;

import com.tatame.connect.exception.model.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleResponseStatusException(
        ResponseStatusException exception,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
        String message = resolveMessage(exception);
        log.warn("Business error. status={}, path={}, message={}", status.value(), request.getRequestURI(), message);
        return ResponseEntity.status(status).body(buildResponse(status, message, request, Map.of()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(
        AccessDeniedException exception,
        HttpServletRequest request
    ) {
        log.warn("Access denied. path={}", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(buildResponse(HttpStatus.FORBIDDEN, "Acesso negado", request, Map.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        Map<String, String> fields = exception.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, fe -> fe.getDefaultMessage() == null ? "invalido" : fe.getDefaultMessage(), (a, b) -> a));
        log.warn("Validation error. path={}, fields={}", request.getRequestURI(), fields);
        return ResponseEntity.badRequest().body(buildResponse(HttpStatus.BAD_REQUEST, "Dados invalidos", request, fields));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(
        ConstraintViolationException exception,
        HttpServletRequest request
    ) {
        log.warn("Constraint violation. path={}, message={}", request.getRequestURI(), exception.getMessage());
        return ResponseEntity.badRequest().body(buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request, Map.of()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(
        DataIntegrityViolationException exception,
        HttpServletRequest request
    ) {
        log.warn("Data integrity violation. path={}", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(buildResponse(HttpStatus.CONFLICT, "Registro ja existe com os dados informados", request, Map.of()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception exception, HttpServletRequest request) {
        log.error("Unexpected error. path={}", request.getRequestURI(), exception);
        return ResponseEntity.internalServerError()
            .body(buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado", request, Map.of()));
    }

    private ApiErrorResponse buildResponse(HttpStatus status, String message, HttpServletRequest request, Map<String, String> fields) {
        return new ApiErrorResponse(OffsetDateTime.now(), status.value(), status.getReasonPhrase(), message, request.getRequestURI(), fields);
    }

    private String resolveMessage(ResponseStatusException exception) {
        return exception.getReason() == null || exception.getReason().isBlank()
            ? exception.getStatusCode().toString()
            : exception.getReason();
    }
}

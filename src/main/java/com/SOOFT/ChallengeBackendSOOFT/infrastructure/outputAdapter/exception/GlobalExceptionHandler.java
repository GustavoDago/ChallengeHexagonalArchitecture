package com.SOOFT.ChallengeBackendSOOFT.infrastructure.outputAdapter.exception;

import com.SOOFT.ChallengeBackendSOOFT.domain.exceptions.EmpresaYaExisteException;
import com.SOOFT.ChallengeBackendSOOFT.infrastructure.inputAdapter.rest.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmpresaYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleEmpresaYaExisteException(EmpresaYaExisteException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}

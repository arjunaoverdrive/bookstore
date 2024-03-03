package org.arjunaoverdrive.bookstore.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.arjunaoverdrive.bookstore.exception.CannotSaveEntityException;
import org.arjunaoverdrive.bookstore.exception.EntityNotFoundException;
import org.arjunaoverdrive.bookstore.web.dto.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDto(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        String errorMessage = String.join(";", errorMessages);

        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(CannotSaveEntityException.class)
    public ResponseEntity<ErrorResponse> handleCannotSaveEntity(CannotSaveEntityException e){
        log.warn(e.getMessage());

        return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e){
        log.warn(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }
}

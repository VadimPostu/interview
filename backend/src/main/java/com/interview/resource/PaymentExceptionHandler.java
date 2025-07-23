package com.interview.resource;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.interview.dto.ErrorDto;
import com.interview.exception.PaymentNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler(PaymentNotFoundException.class)
    ResponseEntity<ErrorDto> handlePaymentNotFound(PaymentNotFoundException exception) {
        log.info("Payment operation failed.", exception);
        ErrorDto errorDto = new ErrorDto("1000", "Payment not found");

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TypeMismatchException.class)
    ResponseEntity<ErrorDto> handleTypeMismatchFound(TypeMismatchException exception) {
        log.info("Type mismatch.", exception);
        ErrorDto errorDto = new ErrorDto("1001", "Type mismatch");

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorDto> handlePaymentNotValid(MethodArgumentNotValidException exception) {
        StringBuilder errorMessage = new StringBuilder("Invalid request.");
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            log.info("Error: {} (Field: {})", error.getDefaultMessage(), error.getField());
            errorMessage.append(" ").append(error.getDefaultMessage());
        });

        ErrorDto errorDto = new ErrorDto("1002", errorMessage.toString().trim());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorDto> handleUnexpectedException(RuntimeException exception) {
        log.info("Unexpected exception caught.", exception);
        ErrorDto errorDto = new ErrorDto("9000", "Internal server error");

        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

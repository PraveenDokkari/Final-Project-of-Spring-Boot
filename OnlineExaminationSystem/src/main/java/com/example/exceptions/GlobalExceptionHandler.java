package com.example.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	 private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	 
	 
	   @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
	        log.warn("Not found: {}", ex.getMessage());
	        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
	    }

	    @ExceptionHandler(BadRequestException.class)
	    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
	        log.warn("Bad request: {}", ex.getMessage());
	        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
	    }

	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
	        Map<String, String> errors = new HashMap<>();
	        ex.getBindingResult().getFieldErrors()
	                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
	        log.warn("Validation failed: {}", errors);
	        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ErrorResponse> handleOthers(Exception ex) {
	        log.error("Unexpected error", ex);
	        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong: " + ex.getMessage(), null);
	    }

	    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message,
	                                                        Map<String, String> errors) {
	        ErrorResponse response = new ErrorResponse();
	        response.setTimestamp(LocalDateTime.now());
	        response.setStatus(status.value());
	        response.setMessage(message);
	        response.setErrors(errors);
	        return new ResponseEntity<>(response, status);
	    }

}

package com.lcwd.user.service.userservice.Excxeption;

import com.lcwd.user.service.userservice.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse>handleResourceNotFoundException(ResourceNotFoundException ex){
        String message = ex.getMessage();
        ApiResponse build = ApiResponse.builder()
                .message(message)
                .success(true)
                .staus(NOT_FOUND)
                .build();
        return new ResponseEntity<ApiResponse>(build, NOT_FOUND);
    }
}

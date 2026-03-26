package com.enterprise.pos.advice;


import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ApiError {
    private HttpStatus status;
    private int statusCode;
    private String message;
    private Boolean isSuspended =  false;
}

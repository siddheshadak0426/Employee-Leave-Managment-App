package com.rak.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler 
{
	@ExceptionHandler(InsufficientLeaveException.class)
	public ResponseEntity<?> insufficientLeaveExceptionHandler(InsufficientLeaveException ex, WebRequest req)
	{
		ErrorDetails error=new ErrorDetails(ex.getMessage(), req.getDescription(true), LocalDateTime.now());
		
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(EmpNotFoundException.class)
	public ResponseEntity<?> empNotFoundExceptionHandler(EmpNotFoundException ex, WebRequest req)
	{
		ErrorDetails error=new ErrorDetails(ex.getMessage(), req.getDescription(true), LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
}

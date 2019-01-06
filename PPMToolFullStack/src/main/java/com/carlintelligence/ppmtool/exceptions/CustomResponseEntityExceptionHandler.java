package com.carlintelligence.ppmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleProjectIDException(ProjectIDException ex, WebRequest  request) {
		
		ProjectIDExceptionResponse excecptionResponse = new ProjectIDExceptionResponse(ex.getMessage());
		
		return new ResponseEntity<>(excecptionResponse, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleProjectNotFoundException(ProjectNotFoundException ex, WebRequest  request) {
		
		ProjectNotFoundExceptionResponse excecptionResponse = new ProjectNotFoundExceptionResponse(ex.getMessage());
		
		return new ResponseEntity<>(excecptionResponse, HttpStatus.BAD_REQUEST);
		
	}

}

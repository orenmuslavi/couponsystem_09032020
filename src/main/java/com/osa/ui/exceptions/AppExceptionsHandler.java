package com.osa.ui.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.osa.ui.model.response.ErrorMessage;

@ControllerAdvice
public class AppExceptionsHandler {

	// global exceptions handler
	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<?> handleExceptions(Exception e) {
		ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

}

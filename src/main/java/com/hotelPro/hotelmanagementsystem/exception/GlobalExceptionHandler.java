package com.hotelPro.hotelmanagementsystem.exception;

import jakarta.servlet.ServletException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.CONFLICT.value());
        errorResponse.setError("A record with the given name/phoneNumber/code already exists");

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = String.format("'%s' should be of type %s", ex.getName(), ex.getRequiredType().getSimpleName());
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(error);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());

        // Get all validation errors
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        body.put("error", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidDataType(HttpMessageNotReadableException ex) {
        // You can customize the error response as needed
        CustomErrorResponse errorDetails = new CustomErrorResponse();
        errorDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetails.setError("Invalid data type in request body. Please ensure all fields have the correct type.");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<Object> handleInvalidEnumValueException(InvalidEnumValueException e) {
        CustomErrorResponse errorDetails = new CustomErrorResponse();
        errorDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetails.setError("Invalid value provided for field " + e.getFieldName());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException ex) {
        CustomErrorResponse errorDetails = new CustomErrorResponse();
        errorDetails.setStatus(ex.getStatus().value());
        errorDetails.setError(ex.getMessage());

        return new ResponseEntity<>(errorDetails, ex.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        CustomErrorResponse errorDetails = new CustomErrorResponse();
        errorDetails.setStatus(HttpStatus.FORBIDDEN.value());
        errorDetails.setError(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<CustomErrorResponse> handleServletException(ServletException ex) {
        CustomErrorResponse errorDetails = new CustomErrorResponse();
        errorDetails.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetails.setError("An unexpected error occurred");
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNotFound(NoHandlerFoundException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError("Not Found");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(Exception.class) // Catch-all for any other exceptions
//    public ResponseEntity<CustomErrorResponse> handleGenericException(Exception ex) {
//        CustomErrorResponse errorResponse = new CustomErrorResponse();
//        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        errorResponse.setError("An unexpected error occurred");
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}

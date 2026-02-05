package org.codegym.springbootdemo.web;

import java.util.HashMap;
import org.codegym.springbootdemo.exception.InsufficientStockException;
import org.codegym.springbootdemo.exception.OrderNotFoundException;
import org.codegym.springbootdemo.exception.ProductNotFoundException;
import org.codegym.springbootdemo.exception.UserAlreadyExistException;
import org.codegym.springbootdemo.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserAlreadyExistException.class)
  public ResponseEntity<String> handleUserAlreadyExistException(UserAlreadyExistException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(InsufficientStockException.class)
  public ResponseEntity<String> handleInsufficientStockException(InsufficientStockException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    HashMap<Object, Object> errors = new HashMap<>();
    e.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
    return ResponseEntity.badRequest().body(errors);
  }

}

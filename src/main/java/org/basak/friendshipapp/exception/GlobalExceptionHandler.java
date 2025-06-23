package org.basak.friendshipapp.exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(FriendshipException.class)
    public ResponseEntity<ErrorMessage> handleFriendshipException(FriendshipException e) {
        return createResponseEntity(e.getErrorType(),
                e.getErrorType().getHttpStatus(),
                null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException e) {
        return createResponseEntity(ErrorType.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> fieldErrors = new ArrayList<>();
        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    fieldErrors.add(fieldError.getField()+" : "+fieldError.getDefaultMessage());
                });
        return createResponseEntity(ErrorType.VALIDATION_ERROR,HttpStatus.BAD_REQUEST,fieldErrors);
    }

    public ResponseEntity<ErrorMessage> createResponseEntity(ErrorType errorType,HttpStatus httpStatus,
                                                             List<String> fieldErrors) {
        log.error(errorType.getCode()+" : "+errorType.getMessage());
        return new ResponseEntity<>(ErrorMessage.builder()
                .code(errorType.getCode())
                .message(errorType.getMessage())
                .success(false)
                .details(fieldErrors).build(),httpStatus);
    }

}

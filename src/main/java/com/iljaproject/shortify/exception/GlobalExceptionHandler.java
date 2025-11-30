package com.iljaproject.shortify.exception;

import com.iljaproject.shortify.dto.ErrorDto;
import com.iljaproject.shortify.dto.ResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ResponseDto<ErrorDto>> urlNotFoundExceptionHandler(Exception exception,
                                                                             WebRequest webRequest){
        ErrorDto error = new ErrorDto(
                webRequest.getDescription(false),
                exception.getClass().getSimpleName(),
                LocalDateTime.now()
        );
        return ResponseDto.error(HttpStatus.NOT_FOUND, exception.getMessage(), error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<ErrorDto>> generalExceptionHandler(Exception exception,
                                                               WebRequest webRequest){
        ErrorDto error = new ErrorDto(
                webRequest.getDescription(false),
                exception.getClass().getSimpleName(),
                LocalDateTime.now()
        );
        return ResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), error);
    }

}

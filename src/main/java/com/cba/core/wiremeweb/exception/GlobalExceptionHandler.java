package com.cba.core.wiremeweb.exception;

import com.cba.core.wiremeweb.dto.ExceptionResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.BindException;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * This is the class handle all the exceptions other than JWT token level authentication exceptions
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException,
                                                                          WebRequest request) {
        BindingResult result = methodArgumentNotValidException.getBindingResult();
        FieldError error = result.getFieldError();
        String errorMessage = error.getDefaultMessage();

        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(LocalDateTime.now(),
                errorMessage,
                request.getDescription(false));

        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<ExceptionResponseDto> handleConstraintValidationException(ConstraintViolationException constraintViolationException,
//                                                                                    WebRequest request) {
//        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(LocalDateTime.now(),
//                constraintViolationException.getMessage(),
//                request.getDescription(false));
//
//        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleNotFoundException(NotFoundException notFoundException,
                                                                        WebRequest request) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(LocalDateTime.now(),
                notFoundException.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.OK);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDto> handleRecordInUseException(DataIntegrityViolationException recordInUseException,
                                                                           WebRequest request) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(LocalDateTime.now(),
                "Record In Use",
                request.getDescription(false));

        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.IM_USED);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public final ResponseEntity<ExceptionResponseDto> handleTokenRefreshException(TokenRefreshException tokenRefreshException,
                                                                                  WebRequest request) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(LocalDateTime.now(),
                tokenRefreshException.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponseDto> handleBindException(BindException ex,
                                                                    WebRequest request) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleAllOtherErrors(Exception exception,
                                                                     WebRequest request) {
        Locale currentLocale = LocaleContextHolder.getLocale();

        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(LocalDateTime.now(),
                messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale),
                request.getDescription(false));

        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

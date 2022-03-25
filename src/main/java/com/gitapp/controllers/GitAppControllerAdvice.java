package com.gitapp.controllers;

import com.gitapp.exceptions.ErrorMessage;
import com.gitapp.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GitAppControllerAdvice {

    private final static String USER_NOT_FOUND_MESSAGE = "User does not exist in github repository";
    private final static String UNSUPPORTED_MEDIA_TYPE = "Unsupported Media Type";
    private final static String BAD_REQUEST_MESSAGE = "Bad request, please check request input";
    private final static String INTERNAL_SERVER_ERROR = "Error occured in application, please try again later";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ErrorMessage> handleBadRequestException(ServerWebInputException exception){
        log.warn("Bad server request",exception);
        return buildErrorMessage(HttpStatus.BAD_REQUEST.value(), BAD_REQUEST_MESSAGE);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ErrorMessage> handleUserNotFound(UserNotFoundException exception){
        log.warn("User not found",exception);
        return buildErrorMessage(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND_MESSAGE);
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(UnsupportedMediaTypeException.class)
    public Mono<ErrorMessage> handleUnsupportedMediaTypeException(UnsupportedMediaTypeException exception){
        log.warn("Unsupported media type used",exception);
        return buildErrorMessage(HttpStatus.NOT_ACCEPTABLE.value(), UNSUPPORTED_MEDIA_TYPE);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Mono<ErrorMessage> handleException(Exception exception){
        log.warn("Exception occured",exception);
        return buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public Mono<ErrorMessage> handleRuntimeException(RuntimeException exception){
        log.warn("Runtime exception occured",exception);
        return buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR);
    }

    private Mono<ErrorMessage> buildErrorMessage(int code, String message){
        ErrorMessage errorMessage = ErrorMessage
                .builder()
                .status(code)
                .message(message)
                .build();

        return Mono.just(errorMessage);
    }
}

package com.gitapp;

import com.gitapp.controllers.GitAppControllerAdvice;
import com.gitapp.exceptions.ErrorMessage;
import com.gitapp.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ControllerAdviceTest {

    @InjectMocks
    private GitAppControllerAdvice gitAppControllerAdvice;

    @Test
    public void should_return_user_not_found(){
        Mono<ErrorMessage> errorMessage = gitAppControllerAdvice.handleUserNotFound(new UserNotFoundException(""));
        StepVerifier.create(errorMessage).expectNextMatches(e -> e.getStatus()==404).verifyComplete();
    }

    @Test
    public void should_return_user_not_acceptable(){
        Mono<ErrorMessage> errorMessage = gitAppControllerAdvice.handleUnsupportedMediaTypeException(new UnsupportedMediaTypeException(""));
        StepVerifier.create(errorMessage).expectNextMatches(e -> e.getStatus()==406).verifyComplete();
    }

    @Test
    public void should_return_bad_request(){
        Mono<ErrorMessage> errorMessage = gitAppControllerAdvice.handleBadRequestException(new ServerWebInputException(""));
        StepVerifier.create(errorMessage).expectNextMatches(e -> e.getStatus()==400).verifyComplete();
    }

    @Test
    public void should_return_internal_error_on_exception(){
        Mono<ErrorMessage> errorMessage = gitAppControllerAdvice.handleException(new Exception());
        StepVerifier.create(errorMessage).expectNextMatches(e -> e.getStatus()==500).verifyComplete();
    }

    @Test
    public void should_return_internal_error_on__runtime_exception(){
        Mono<ErrorMessage> errorMessage = gitAppControllerAdvice.handleRuntimeException(new RuntimeException());
        StepVerifier.create(errorMessage).expectNextMatches(e -> e.getStatus()==500).verifyComplete();
    }
}

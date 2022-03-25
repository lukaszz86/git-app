package com.gitapp.controllers;

import com.gitapp.model.GitRepositorySummary;
import com.gitapp.services.GitRepositorySummaryService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import reactor.core.publisher.Flux;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GitRepositoryController {

    private final static String UNSUPPORTED_MEDIA_TYPE_MESSAGE = "Unsupported Media Type used";

    private final GitRepositorySummaryService gitRepositorySummaryService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lists all repositories for given user that are not forked"),
            @ApiResponse(responseCode = "400", description = "Wrong request, please check input"),
            @ApiResponse(responseCode = "404", description = "User with no repositories on github"),
            @ApiResponse(responseCode = "406", description = "Incorrect media type used"),
            @ApiResponse(responseCode = "500", description = "General application error"),
    })
    @GetMapping(value = "api/users/{username}/repositories")
    public Flux<GitRepositorySummary> retrieveSummary(
            @PathVariable String username,
            @RequestHeader(name = HttpHeaders.CONTENT_TYPE, required = false) String contentType) {
        if(Objects.nonNull(contentType) && !contentType.equals(MediaType.APPLICATION_JSON_VALUE)){
            log.error(UNSUPPORTED_MEDIA_TYPE_MESSAGE);
            return Flux.error(new UnsupportedMediaTypeException(UNSUPPORTED_MEDIA_TYPE_MESSAGE));
        }
        return gitRepositorySummaryService
                .combineGitRepositorySummary(username)
                .doOnComplete(() -> log.info("Retrieving summary"))
                .doOnError(error -> log.error("Not able to retrieve summary"));
    }
}

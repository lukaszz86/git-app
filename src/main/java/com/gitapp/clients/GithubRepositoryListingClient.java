package com.gitapp.clients;

import com.gitapp.exceptions.UserNotFoundException;
import com.gitapp.model.listing.GitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.gitapp.constants.Constants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubRepositoryListingClient {

    private final static String USER_NOT_FOUND_MESSAGE = "User does not exist in github";

    private final WebClient.Builder webClient;

    @Value("${host:api.github.com}")
    private String host;

    public Flux<GitRepository> retrieveGitRepository(String username){
        return webClient
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme(SCHEME)
                        .host(host)
                        .path(GIT_REPO_LISTING)
                        .build(username))
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        response -> Mono.error(new UserNotFoundException(USER_NOT_FOUND_MESSAGE)))
                .bodyToFlux(GitRepository.class)
                .doOnComplete(()->log.info("Repositories retrieved"))
                .doOnError(error->log.error("Error while retrieving repositories"));
    }
}
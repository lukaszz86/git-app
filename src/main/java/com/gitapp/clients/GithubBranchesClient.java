package com.gitapp.clients;

import com.gitapp.model.branches.Branch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static com.gitapp.constants.Constants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubBranchesClient {

    @Value("${host:api.github.com}")
    private String host;

    private final WebClient.Builder webClient;

    public Flux<Branch> retrieveBranches(String username, String repositoryName){
        return webClient
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme(SCHEME)
                        .host(host)
                        .path(GIT_REPO_BRANCHES)
                        .build(username,repositoryName))
                .retrieve()
                .bodyToFlux(Branch.class)
                .doOnComplete(()->log.info("Branches retrieved"))
                .doOnError(error->log.error("Error while retrieving branches"));
    }
}

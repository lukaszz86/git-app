package com.gitapp.services;

import com.gitapp.clients.GithubRepositoryListingClient;
import com.gitapp.model.listing.GitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GitRepositoryListingService {

    private final GithubRepositoryListingClient githubRepositoryListingClient;

    public Flux<GitRepository> listUserGitRepositories(String username){
        return githubRepositoryListingClient.retrieveGitRepository(username);
    }
}

package com.gitapp.services;

import com.gitapp.clients.GithubBranchesClient;
import com.gitapp.model.branches.Branch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GitBranchesService {

    private final GithubBranchesClient githubBranchesClient;

    public Flux<Branch> retrieveBranches(String username, String repositoryName) {
        return githubBranchesClient.retrieveBranches(username, repositoryName);
    }
}
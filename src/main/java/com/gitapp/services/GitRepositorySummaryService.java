package com.gitapp.services;

import com.gitapp.model.BranchSummary;
import com.gitapp.model.GitRepositorySummary;
import com.gitapp.model.branches.Branch;
import com.gitapp.model.listing.GitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitRepositorySummaryService {

    private final GitBranchesService gitBranchesService;

    private final GitRepositoryListingService gitRepositoryListingService;

    public Flux<GitRepositorySummary> combineGitRepositorySummary(String username){
        return gitRepositoryListingService.listUserGitRepositories(username)
                .filter(gitRepository -> !gitRepository.isFork())
                .collectList()
                .flatMapMany(gitRepositories -> runMappingWithBranchesInParallel(username,gitRepositories));
    }

    private Flux<GitRepositorySummary> runMappingWithBranchesInParallel(String username, List<GitRepository> gitRepositories) {
        return Flux.fromIterable(gitRepositories)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(gitRepository -> mapRepositoryWithBranches(username, gitRepository))
                .sequential();
    }

    private Mono<GitRepositorySummary> mapRepositoryWithBranches(String username, GitRepository gitRepository) {
        String repositoryName = gitRepository.getRepositoryName();
        String ownerName = gitRepository.getOwner().getLogin();
        return gitBranchesService.retrieveBranches(username, repositoryName)
                .flatMap(branch -> buildBranchSummary(branch))
                .collectList()
                .flatMap(branches -> buildGitRepositorySummary(ownerName, repositoryName, branches));
    }

    private Mono<BranchSummary> buildBranchSummary(Branch branch) {
        return Mono.just(BranchSummary.builder()
                .branchName(branch.getName())
                .commitSha(branch.getCommit().getSha())
                .build());
    }

    private Mono<GitRepositorySummary> buildGitRepositorySummary(String owner, String repoName, List<BranchSummary> branches){
        return Mono.just(GitRepositorySummary.builder()
                .branches(branches)
                .ownerName(owner)
                .repositoryName(repoName)
                .build());
    }

}

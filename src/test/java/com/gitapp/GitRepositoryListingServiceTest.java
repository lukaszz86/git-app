package com.gitapp;

import com.gitapp.clients.GithubRepositoryListingClient;
import com.gitapp.model.listing.GitRepository;
import com.gitapp.model.listing.Owner;
import com.gitapp.services.GitRepositoryListingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GitRepositoryListingServiceTest {

    private final static String USER = "lukaszz86";
    private static final String REPO_UTIL = "repo-util";

    @Mock
    private GitRepository gitRepository;

    @Mock
    private GithubRepositoryListingClient githubRepositoryListingClient;

    @InjectMocks
    private GitRepositoryListingService gitRepositoryListingService;

    @Test
    public void should_return_a_list_of_branches_for_given_user() {

        Owner owner = new Owner();
        owner.setLogin(USER);

        GitRepository gitRepository = new GitRepository();
        gitRepository.setOwner(owner);
        gitRepository.setFork(false);
        gitRepository.setRepositoryName(REPO_UTIL);

        PublisherProbe publisherProbe = PublisherProbe.of(Flux.just(gitRepository));
        when(githubRepositoryListingClient.retrieveGitRepository(USER)).thenReturn(publisherProbe.flux());

        Flux<GitRepository> actual = gitRepositoryListingService.listUserGitRepositories(USER);

        StepVerifier.create(actual)
                .expectNextMatches(gitRepository1 -> gitRepository1.getRepositoryName().equals(REPO_UTIL))
                .verifyComplete();

        verify(githubRepositoryListingClient, only()).retrieveGitRepository(USER);
    }
}

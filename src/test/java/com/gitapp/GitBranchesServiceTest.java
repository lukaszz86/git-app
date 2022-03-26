package com.gitapp;

import com.gitapp.clients.GithubBranchesClient;
import com.gitapp.model.branches.Branch;
import com.gitapp.model.branches.Commit;
import com.gitapp.services.GitBranchesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GitBranchesServiceTest {

    private final static String USER = "lukaszz86";
    private static final String REPO_UTIL = "repo-util";
    private static final String COMMIT = "23fs5g2";

    @Mock
    private Branch branch;

    @Mock
    private GithubBranchesClient githubBranchesClient;

    @InjectMocks
    private GitBranchesService gitBranchesService;

    @Test
    public void should_return_a_list_of_branches_for_given_user(){

        //given
        Commit commit = new Commit();
        commit.setSha(COMMIT);

        Branch branch = new Branch();
        branch.setName(REPO_UTIL);
        branch.setCommit(commit);

        PublisherProbe publisherProbe = PublisherProbe.of(Flux.just(branch));

        given(githubBranchesClient.retrieveBranches(USER, REPO_UTIL)).willReturn(publisherProbe.flux());

        //when
        Flux<Branch> actual = gitBranchesService.retrieveBranches(USER, REPO_UTIL);

        //then
        StepVerifier.create(actual)
                .expectNextMatches(branch1 -> branch1.getCommit().getSha().equals(COMMIT))
                .verifyComplete();

        verify(githubBranchesClient, only()).retrieveBranches(USER, REPO_UTIL);
    }
}

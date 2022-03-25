package com.gitapp.constants;

public interface Constants {
    String SCHEME = "https";
    String HOST = "api.github.com";
    String GIT_REPO_LISTING = "/users/{username}/repos";
    String GIT_REPO_BRANCHES = "/repos/{username}/{repositoryName}/branches";
}

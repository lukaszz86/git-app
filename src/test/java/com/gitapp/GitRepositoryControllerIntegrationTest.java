package com.gitapp;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class GitRepositoryControllerIntegrationTest {

    private final static String REPO_UTIL = "repo-util";
    private final static String MAIN_BRANCH = "main";
    private final static String TESTING_REPO = "testing";
    private final static String DEV_BRANCH = "dev";
    private final static String TEST_URI_PATH = "/api/users/lukaszz86/repositories";
    private final static String PATH = "api/users/{username}/repositories";
    private final static String TEST_RESOURCES = "src/test/resources/";
    private final static String USER = "lukaszz86";
    private final static String USER_NOT_FOUND_MESSAGE = "User does not exist in github repository";
    private final static String NOT_ACCEPTABLE_MESSAGE = "Unsupported Media Type";
    private final static String PARSING_ERROR_MESSAGE = "Not able to parse json file";
    private final static String GIT_REPO_LISTING_FILE = "repository_list.json";
    private final static String GIT_REPO_NOT_FOUND_FILE = "repository_not_found.json";
    private final static String GIT_REPO_NOT_ACCEPTABLE_FILE = "repository_not_acceptable.json";

    private WireMockServer wireMockServer;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        webTestClient = WebTestClient.bindToServer().baseUrl(wireMockServer.baseUrl()).build();
    }

    @Test
    void should_return_ok_response() {
        //given
        //when
        wireMockServer.stubFor(
                WireMock.get(TEST_URI_PATH)
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(readResource(GIT_REPO_LISTING_FILE))));

        //then
        webTestClient.get()
                .uri(PATH,USER)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].repositoryName").isEqualTo(REPO_UTIL)
                .jsonPath("$[0].ownerName").isEqualTo(USER)
                .jsonPath("$[0].branches[0].branchName").isEqualTo(MAIN_BRANCH)
                .jsonPath("$[1].repositoryName").isEqualTo(TESTING_REPO)
                .jsonPath("$[1].ownerName").isEqualTo(USER)
                .jsonPath("$[1].branches[0].branchName").isEqualTo(DEV_BRANCH);
    }

    @Test
    void should_return_status_not_found_with_proper_message() {
        //given
        //when
        wireMockServer.stubFor(
                WireMock.get(TEST_URI_PATH)
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.NOT_FOUND.value())
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(readResource(GIT_REPO_NOT_FOUND_FILE))));

        //then
        webTestClient.get()
                .uri(PATH,USER)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.value())
                .jsonPath("$.message").isEqualTo(USER_NOT_FOUND_MESSAGE);
    }

    @Test
    void should_return_status_not_acceptable_with_proper_message() {
        //given
        //when
        wireMockServer.stubFor(
                WireMock.get(TEST_URI_PATH)
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.NOT_ACCEPTABLE.value())
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(readResource(GIT_REPO_NOT_ACCEPTABLE_FILE))));

        //then
        webTestClient.get()
                .uri(PATH,USER)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_ACCEPTABLE)
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_ACCEPTABLE.value())
                .jsonPath("$.message").isEqualTo(NOT_ACCEPTABLE_MESSAGE);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    private String readResource(String path){
        try {
            Path resourcePath = Paths.get(TEST_RESOURCES+path);
            return Files.readAllLines(resourcePath, StandardCharsets.UTF_8)
                    .stream()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException(PARSING_ERROR_MESSAGE);
        }
    }
}

package org.demo.atiperataskspring.client;

import org.demo.atiperataskspring.exception.GithubUserNotFoundException;
import org.demo.atiperataskspring.model.Branch;
import org.demo.atiperataskspring.model.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Controller
public class GithubClient {
    private final RestClient restClient;

    public GithubClient(@Value("${github.api.url}") String baseUrl) {

        this.restClient = RestClient.builder()
                .defaultHeader("Accept", "application/vnd.github.v3+json")
                .baseUrl(baseUrl)
                .build();
    }

    public List<Repository> getUserRepositories(String username) {
        try {

            return restClient.get()
                    .uri("/users/" + username + "/repos")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (HttpClientErrorException.NotFound e) {
            throw new GithubUserNotFoundException("User not found");
        }
    }

    public List<Branch> getBranches(String owner, String repoName) {
        try {
            return restClient.get()
                    .uri("/repos/" + owner + "/" + repoName + "/branches")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e);
        }
    }

}

package org.demo.atiperataskspring.service;

import org.demo.atiperataskspring.client.GithubClient;
import org.demo.atiperataskspring.model.Branch;
import org.demo.atiperataskspring.model.Repository;
import org.demo.atiperataskspring.model.response.RepositoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoriesService {
    private final GithubClient githubClient;

    public RepositoriesService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<RepositoryResponse> getRepositories(String username) {
        List<Repository> filteredRepos = githubClient.getUserRepositories(username)
                .stream()
                .filter(repo -> !repo.isFork())
                .toList();

        return filteredRepos.stream()
                .map(repo -> {
                    List<Branch> branches = githubClient.getBranches(repo.getOwnerLogin(), repo.getName());
                    return new RepositoryResponse(repo.getName(), repo.getOwnerLogin(), branches);
                }).toList();
    }
}

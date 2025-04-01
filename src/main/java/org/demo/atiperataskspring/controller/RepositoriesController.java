package org.demo.atiperataskspring.controller;

import org.demo.atiperataskspring.model.response.RepositoryResponse;
import org.demo.atiperataskspring.service.RepositoriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/repositories")
public class RepositoriesController {
    private final RepositoriesService repositoriesService;

    public RepositoriesController(RepositoriesService repositoriesService) {
        this.repositoriesService = repositoriesService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<RepositoryResponse>> getRepositories(
            @PathVariable String username
    ) {
        List<RepositoryResponse> reposResponse = repositoriesService.getRepositories(username);

        return ResponseEntity.ok(reposResponse);
    }
}

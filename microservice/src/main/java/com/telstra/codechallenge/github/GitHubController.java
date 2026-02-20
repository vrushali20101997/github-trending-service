package com.telstra.codechallenge.github;

import com.telstra.codechallenge.github.model.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/repositories")
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/hottest")
    public ResponseEntity<List<Repository>> getHottestRepositories(
            @RequestParam(defaultValue = "10") int limit) {

        List<Repository> repositories = gitHubService.getHottestRepositories(limit);
        return ResponseEntity.ok(repositories);
    }
}
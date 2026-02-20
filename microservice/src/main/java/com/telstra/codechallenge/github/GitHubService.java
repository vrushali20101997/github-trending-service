package com.telstra.codechallenge.github;
import org.springframework.cache.annotation.Cacheable;
import com.telstra.codechallenge.github.model.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitHubService {

    private final GitHubClient gitHubClient;

    public GitHubService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @Cacheable(value = "hottest-repos", key = "#limit")
    public List<Repository> getHottestRepositories(int limit) {
        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException("limit must be between 1 and 100");
        }
        return gitHubClient.fetchHottestRepositories(limit);
    }
}
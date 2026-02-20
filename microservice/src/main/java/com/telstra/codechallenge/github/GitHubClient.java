package com.telstra.codechallenge.github;

import com.telstra.codechallenge.github.model.GitHubSearchResponse;
import com.telstra.codechallenge.github.model.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Component
public class GitHubClient {

    private static final Logger log = LoggerFactory.getLogger(GitHubClient.class);

    @Value("${github.base.url}")
    private String githubBaseUrl;

    private final RestTemplate restTemplate;

    public GitHubClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Repository> fetchHottestRepositories(int limit) {
    String createdAfter = LocalDate.now().minusDays(7)
            .format(DateTimeFormatter.ISO_LOCAL_DATE);

    String url = githubBaseUrl
            + "/search/repositories"
            + "?q=created:>=" + createdAfter
            + "&sort=stars"
            + "&order=desc"
            + "&per_page=" + limit;

    log.info("Calling GitHub API: {}", url);

    try {
        GitHubSearchResponse response = restTemplate.getForObject(url, GitHubSearchResponse.class);

        if (response == null || response.getItems() == null) {
            log.warn("GitHub API returned empty response");
            return Collections.emptyList();
        }

        return response.getItems();

    } catch (RestClientException ex) {
        log.error("GitHub API call failed: {}", ex.getMessage());
        throw new RuntimeException("Unable to reach GitHub API. Please try again later.", ex);
    }
}
}
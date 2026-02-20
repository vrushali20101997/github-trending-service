package com.telstra.codechallenge.github;

import com.telstra.codechallenge.github.model.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    @Mock
    private GitHubClient gitHubClient;

    private GitHubService service;

    @BeforeEach
    void setUp() {
        service = new GitHubService(gitHubClient);
    }

    @Test
    void getHottestRepositories_returnsResultsFromClient() {
        List<Repository> expected = List.of(new Repository());
        given(gitHubClient.fetchHottestRepositories(5)).willReturn(expected);

        List<Repository> result = service.getHottestRepositories(5);

        assertThat(result).isEqualTo(expected);
        verify(gitHubClient).fetchHottestRepositories(5);
    }

    @Test
    void getHottestRepositories_throwsWhenLimitIsZero() {
        assertThatThrownBy(() -> service.getHottestRepositories(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("limit must be between 1 and 100");
    }

    @Test
    void getHottestRepositories_throwsWhenLimitExceeds100() {
        assertThatThrownBy(() -> service.getHottestRepositories(101))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("limit must be between 1 and 100");
    }

    @Test
    void getHottestRepositories_acceptsBoundaryValues() {
        given(gitHubClient.fetchHottestRepositories(1)).willReturn(List.of());
        given(gitHubClient.fetchHottestRepositories(100)).willReturn(List.of());

        assertThat(service.getHottestRepositories(1)).isEmpty();
        assertThat(service.getHottestRepositories(100)).isEmpty();
    }
}
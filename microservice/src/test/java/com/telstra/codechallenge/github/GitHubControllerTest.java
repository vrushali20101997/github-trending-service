package com.telstra.codechallenge.github;
import org.springframework.context.annotation.Import;
import com.telstra.codechallenge.WebSecurityConfig;
import com.telstra.codechallenge.github.model.Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.telstra.codechallenge.GlobalExceptionHandler;

@WebMvcTest(GitHubController.class)
@Import({WebSecurityConfig.class, GlobalExceptionHandler.class}) 
class GitHubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubService gitHubService;

    @Test
    void getHottestRepositories_returns200WithResults() throws Exception {
        Repository repo = new Repository();
        repo.setName("awesome-repo");
        repo.setHtmlUrl("https://github.com/foo/awesome-repo");
        repo.setWatchersCount(999);
        repo.setLanguage("Java");
        repo.setDescription("An awesome repo");

        given(gitHubService.getHottestRepositories(2)).willReturn(List.of(repo));

        mockMvc.perform(get("/repositories/hottest").param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("awesome-repo"))
                .andExpect(jsonPath("$[0].watchers_count").value(999));
    }

    @Test
    void getHottestRepositories_usesDefaultLimitOf10() throws Exception {
        given(gitHubService.getHottestRepositories(10)).willReturn(List.of());

        mockMvc.perform(get("/repositories/hottest"))
                .andExpect(status().isOk());
    }
    @Test
void getHottestRepositories_returns400WhenLimitIsZero() throws Exception {
    given(gitHubService.getHottestRepositories(0))
            .willThrow(new IllegalArgumentException("limit must be between 1 and 100"));

    mockMvc.perform(get("/repositories/hottest").param("limit", "0"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").value("limit must be between 1 and 100"));
}

@Test
void getHottestRepositories_returns400WhenLimitExceeds100() throws Exception {
    given(gitHubService.getHottestRepositories(101))
            .willThrow(new IllegalArgumentException("limit must be between 1 and 100"));

    mockMvc.perform(get("/repositories/hottest").param("limit", "101"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").value("limit must be between 1 and 100"));
}
}
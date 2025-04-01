package org.demo.atiperataskspring;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.demo.atiperataskspring.model.response.RepositoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8888)
@TestPropertySource(properties = "github.api.url=http://localhost:8888")
public class RepositoryIT {

    @LocalServerPort
    private int port;

    private RestClient testRestClient;

    @BeforeEach
    void setup() {

        testRestClient = RestClient.builder()
                .baseUrl("http://localhost:" + port + "/api/v1")
                .build();

        // Mock: GitHub API – user repos
        stubFor(get(urlEqualTo("/users/someuser/repos"))
                .willReturn(okJson("""
                                [
                                  {
                                    "name": "hello-world",
                                    "fork": false,
                                    "owner": { "login": "someuser" }
                                  },
                                  {
                                    "name": "ForkedRepo",
                                    "fork": true,
                                    "owner": { "login": "someuser" }
                                  }
                                ]
                        """)));

        // Mock: GitHub API – branches
        stubFor(get(urlEqualTo("/repos/someuser/hello-world/branches"))
                .willReturn(okJson("""
                                [
                                  {
                                    "name": "main",
                                    "commit": {
                                      "sha": "abc123"
                                    }
                                  }
                                ]
                        """)));
    }

    @Test
    void shouldReturnRepositoriesFromOurApiController() {
        List<RepositoryResponse> response = testRestClient.get()
                .uri("/repositories/someuser")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        assertThat(response).isNotNull();
        assertEquals(1, response.size());

        RepositoryResponse repo = response.getFirst();

        assertThat(repo.name()).isEqualTo("hello-world");
        assertThat(repo.owner()).isEqualTo("someuser");

        var branches = repo.branches();

        assertThat(branches).isNotNull();

        var branch = branches.getFirst();

        assertThat(branch.getName()).isEqualTo("main");
        assertThat(branch.getLastCommitSha()).isEqualTo("abc123");
    }

    @Test
    void shouldReturn404WhenUserNotFound() {
        var username = UUID.randomUUID();

        stubFor(get(urlEqualTo("/users/" + username + "/repos"))
                .willReturn(aResponse().withStatus(404)));

        try {
            testRestClient.get()
                    .uri("/repositories/" + username)
                    .retrieve()
                    .toEntity(String.class);

            fail("Expected HttpClientErrorException.NotFound to be thrown");
        } catch (HttpClientErrorException.NotFound ex) {
            assertThat(ex.getStatusCode().value()).isEqualTo(404);
            assertThat(ex.getResponseBodyAsString()).contains("User not found");
            assertThat(ex.getResponseBodyAsString()).contains("404");

        }
    }
}


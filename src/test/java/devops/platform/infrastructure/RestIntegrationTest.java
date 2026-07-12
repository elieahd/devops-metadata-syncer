package devops.platform.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public abstract class RestIntegrationTest extends IntegrationTest {

    @LocalServerPort
    protected int port;
    protected RestClient restClient;

    @BeforeEach
    void setUpRestClient() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultStatusHandler(_ -> true, (_, _) -> {
                })
                .build();
    }

    protected <T> ResponseEntity<T> post(String uri, Object payload, Class<T> responseType) {
        return restClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toEntity(responseType);
    }

    protected <T> ResponseEntity<T> put(String uri, Object payload, Class<T> responseType) {
        return restClient.put()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toEntity(responseType);
    }

    protected <T> ResponseEntity<T> put(String uri, Class<T> responseType) {
        return restClient.put()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(responseType);
    }

    protected <T> ResponseEntity<T> get(String uri, Class<T> responseType) {
        return restClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(responseType);
    }

    protected <T> ResponseEntity<T> get(String uri, ParameterizedTypeReference<T> responseType) {
        return restClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(responseType);
    }

}
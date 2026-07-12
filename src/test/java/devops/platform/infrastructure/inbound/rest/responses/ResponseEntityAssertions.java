package devops.platform.infrastructure.inbound.rest.responses;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.springframework.http.ResponseEntity;

public class ResponseEntityAssertions extends AbstractAssert<ResponseEntityAssertions, ResponseEntity<?>> {

    public ResponseEntityAssertions(ResponseEntity<?> actual) {
        super(actual, ResponseEntityAssertions.class);
    }

    public static ResponseEntityAssertions assertThat(ResponseEntity<?> actual) {
        return new ResponseEntityAssertions(actual);
    }

    public ResponseEntityAssertions is200() {
        isNotNull();
        assertThatStatusCode(200);
        return this;
    }

    public ResponseEntityAssertions is404() {
        isNotNull();
        assertThatStatusCode(404);
        return this;
    }

    public ResponseEntityAssertions is400() {
        isNotNull();
        assertThatStatusCode(400);
        return this;
    }

    public ResponseEntityAssertions is201() {
        isNotNull();
        assertThatStatusCode(201);
        return this;
    }

    public ResponseEntityAssertions is202() {
        isNotNull();
        assertThatStatusCode(202);
        return this;
    }

    private void assertThatStatusCode(int code) {
        Assertions.assertThat(actual.getStatusCode().value()).isEqualTo(code);
    }

    public ResponseEntityAssertions is204() {
        isNotNull();
        assertThatStatusCode(204);
        return this;
    }
}
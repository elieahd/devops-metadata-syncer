package devops.platform.infrastructure.inbound.rest.responses;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ErrorResponseAssertions extends AbstractAssert<ErrorResponseAssertions, ErrorResponse> {

    public ErrorResponseAssertions(ErrorResponse actual) {
        super(actual, ErrorResponseAssertions.class);
    }

    public static ErrorResponseAssertions assertThat(ErrorResponse actual) {
        return new ErrorResponseAssertions(actual);
    }

    public ErrorResponseAssertions hasCode(String code) {
        isNotNull();
        Assertions.assertThat(actual.errorCode()).isEqualTo(code);
        return this;
    }

    public ErrorResponseAssertions hasMessage(String message) {
        isNotNull();
        Assertions.assertThat(actual.errorMessage()).isEqualTo(message);
        return this;
    }
}
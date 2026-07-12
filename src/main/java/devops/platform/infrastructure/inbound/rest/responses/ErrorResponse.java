package devops.platform.infrastructure.inbound.rest.responses;

public record ErrorResponse(String errorCode,
                            String errorMessage) {
}
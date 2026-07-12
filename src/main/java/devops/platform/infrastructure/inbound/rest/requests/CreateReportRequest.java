package devops.platform.infrastructure.inbound.rest.requests;

public record CreateReportRequest(String type,
                                  String status,
                                  String metadata) {
}

package devops.platform.domain.models;

import java.time.LocalDateTime;

public record Report(ReportType type,
                     ReportStatus status,
                     LocalDateTime createdAt,
                     String metadata) {

    public Report(ReportType type,
                  ReportStatus status,
                  String metadata) {
        this(type, status, LocalDateTime.now(), metadata);
    }
}

package devops.platform.domain.exceptions;

import devops.platform.domain.models.ReportStatus;

import java.util.Arrays;

public class InvalidReportStatusException extends Exception {

    public InvalidReportStatusException(String status) {
        super("'%s' report status is invalid, available options : %s".formatted(status, Arrays.toString(ReportStatus.values())));
    }

}

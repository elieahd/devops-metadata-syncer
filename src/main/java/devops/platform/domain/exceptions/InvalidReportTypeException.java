package devops.platform.domain.exceptions;

import devops.platform.domain.models.ReportType;

import java.util.Arrays;

public class InvalidReportTypeException extends Exception {

    public InvalidReportTypeException(String type) {
        super("'%s' report type is invalid, available options: %s".formatted(type, Arrays.toString(ReportType.values())));
    }

}

package devops.platform.domain.exceptions;

public class InvalidReportStatusException extends Exception {

    public InvalidReportStatusException(String status) {
        super("'%s' report status is invalid".formatted(status));
    }

}

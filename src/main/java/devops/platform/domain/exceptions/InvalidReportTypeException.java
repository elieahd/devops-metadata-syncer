package devops.platform.domain.exceptions;

public class InvalidReportTypeException extends Exception {

    public InvalidReportTypeException(String type) {
        super("'%s' report type is invalid".formatted(type));
    }

}

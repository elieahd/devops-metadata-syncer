package devops.platform.infrastructure.inbound.rest.exceptions;

import devops.platform.domain.exceptions.InvalidReportStatusException;
import devops.platform.domain.exceptions.InvalidReportTypeException;
import devops.platform.domain.exceptions.ProjectNotFoundException;
import devops.platform.domain.exceptions.RepositoryNotFoundException;
import devops.platform.domain.exceptions.SourceNotFoundException;
import devops.platform.infrastructure.inbound.rest.responses.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler({
            ProjectNotFoundException.class,
            RepositoryNotFoundException.class,
            SourceNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception ex) {
        return handleError(NOT_FOUND, ex);
    }

    @ExceptionHandler({
            InvalidReportStatusException.class,
            InvalidReportTypeException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        return handleError(BAD_REQUEST, ex);
    }

    private ResponseEntity<ErrorResponse> handleError(HttpStatus httpStatus, Exception ex) {
        LOGGER.warn("[{}}:{}] {}", httpStatus.value(), ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity
                .status(httpStatus.value())
                .body(new ErrorResponse(httpStatus.name(), ex.getMessage()));
    }
}
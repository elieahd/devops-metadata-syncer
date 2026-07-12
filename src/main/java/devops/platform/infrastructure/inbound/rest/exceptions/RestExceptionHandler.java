package devops.platform.infrastructure.inbound.rest.exceptions;

import devops.platform.domain.exceptions.InvalidReportStatusException;
import devops.platform.domain.exceptions.InvalidReportTypeException;
import devops.platform.domain.exceptions.ProjectNotFoundException;
import devops.platform.domain.exceptions.RepositoryNotFoundException;
import devops.platform.domain.exceptions.SourceNotFoundException;
import devops.platform.infrastructure.inbound.rest.responses.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        LOGGER.warn("[404:{}] {}", ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity
                .status(404)
                .body(new ErrorResponse(NOT_FOUND.name(), ex.getMessage()));
    }

    @ExceptionHandler({
            InvalidReportStatusException.class,
            InvalidReportTypeException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        LOGGER.warn("[400:{}] {}", ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity
                .status(400)
                .body(new ErrorResponse(BAD_REQUEST.name(), ex.getMessage()));
    }

}
package devops.platform.domain.inbound;

import devops.platform.domain.exceptions.InvalidReportStatusException;
import devops.platform.domain.exceptions.InvalidReportTypeException;
import devops.platform.domain.exceptions.ProjectNotFoundException;

public interface CreateProjectReport {

    void create(String projectKey,
                String type,
                String status,
                String metadata) throws ProjectNotFoundException, InvalidReportTypeException, InvalidReportStatusException;

}

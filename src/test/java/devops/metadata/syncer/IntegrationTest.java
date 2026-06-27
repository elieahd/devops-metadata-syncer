package devops.metadata.syncer;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers
@DirtiesContext
@ActiveProfiles("test")
public abstract class IntegrationTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer POSTGRES_CONTAINER = new PostgreSQLContainer("postgres:latest");

}

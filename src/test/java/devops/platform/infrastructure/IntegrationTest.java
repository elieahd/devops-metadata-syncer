package devops.platform.infrastructure;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
@ActiveProfiles("test")
public abstract class IntegrationTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer POSTGRES_CONTAINER = new PostgreSQLContainer("postgres:latest");

}

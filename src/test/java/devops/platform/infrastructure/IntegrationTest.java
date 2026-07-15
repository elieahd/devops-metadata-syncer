package devops.platform.infrastructure;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.Repository;
import devops.platform.domain.models.RepositorySource;
import devops.platform.domain.models.randomizers.ProjectRandomizer;
import devops.platform.domain.outbound.ProjectInventory;
import devops.platform.domain.outbound.RepositoryInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static com.devt.randomizer.RandomizerUtils.random;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
@ActiveProfiles("test")
public abstract class IntegrationTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer POSTGRES_CONTAINER = new PostgreSQLContainer("postgres:latest");

    @Autowired
    protected ProjectInventory projectInventory;

    @Autowired
    protected RepositoryInventory repositoryInventory;

    protected Project createProject() {
        String key = ProjectRandomizer.key();
        String name = random(String.class);
        Project project = Project.of(key, name);
        return projectInventory.create(project);
    }

    protected Repository createRepository(Project project) {
        String organization = random(String.class);
        String name = random(String.class);
        RepositorySource source = random(RepositorySource.class);
        Repository repository = Repository.of(organization, name, source);
        return repositoryInventory.create(project.id(), repository);
    }
}

package devops.metadata.syncer.infrastructure.inbound.it;

import devops.metadata.syncer.IntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(args = {
        "--command=sync-project",
        "--project=my-project"
})


@TestPropertySource(
        properties = "spring.liquibase.change-log=classpath:db/changelog/test/sync-project-test-changelog.xml"
)
class SyncProjectIntegrationTest extends IntegrationTest {

}

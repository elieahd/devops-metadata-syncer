package devops.metadata.syncer.infrastructure;

import devops.metadata.syncer.IntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("outbound-test")
public abstract class OutboundDatabaseIntegrationTest extends IntegrationTest {
}
package devops.platform.domain.models.assertions;

import devops.platform.domain.models.Report;
import devops.platform.domain.models.ReportStatus;
import devops.platform.domain.models.ReportType;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.within;

public class ReportAssertions extends AbstractAssert<ReportAssertions, Report> {

    public ReportAssertions(Report actual) {
        super(actual, ReportAssertions.class);
    }

    public static ReportAssertions assertThat(Report actual) {
        return new ReportAssertions(actual);
    }

    public ReportAssertions isEqualTo(Report expected) {

        isNotNull();

        hasType(expected.type());
        hasStatus(expected.status());
        Assertions.assertThat(actual.createdAt()).isEqualTo(expected.createdAt());
        hasMetadata(expected.metadata());

        return this;
    }

    public ReportAssertions hasType(ReportType type) {
        isNotNull();
        Assertions.assertThat(actual.type()).isEqualTo(type);
        return this;
    }

    public ReportAssertions hasStatus(ReportStatus status) {
        isNotNull();
        Assertions.assertThat(actual.status()).isEqualTo(status);
        return this;
    }

    public ReportAssertions hasMetadata(String metadata) {
        isNotNull();

        JsonMapper json = JsonMapper.builder().build();

        Map<String, String> metadataProperties = json.readValue(actual.metadata(), Map.class);
        Map<String, String> expectedMetadataProperties = json.readValue(metadata, Map.class);

        Assertions.assertThat(metadataProperties).containsAllEntriesOf(expectedMetadataProperties);
        return this;
    }

    public ReportAssertions hasCreatedDateAsNow() {
        isNotNull();
        Assertions.assertThat(actual.createdAt()).isCloseTo(LocalDateTime.now(), within(Duration.ofSeconds(5)));
        return this;
    }

}

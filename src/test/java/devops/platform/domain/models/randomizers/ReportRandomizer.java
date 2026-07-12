package devops.platform.domain.models.randomizers;

import com.devt.randomizer.RandomizerUtils;
import devops.platform.domain.models.Report;
import devops.platform.domain.models.ReportStatus;
import devops.platform.domain.models.ReportType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.Map;

public class ReportRandomizer {

    private final ReportType type;
    private final ReportStatus status;
    private final LocalDateTime createdAt;
    private final String metadata;

    public ReportRandomizer() {
        this.type = RandomizerUtils.random(ReportType.class);
        this.status = RandomizerUtils.random(ReportStatus.class);
        this.createdAt = RandomizerUtils.random(LocalDateTime.class);
        this.metadata = metadata();
    }

    public static String metadata() {
        Map<String, String> metadata = Map.of(
                "key", RandomizerUtils.random(String.class),
                "label", RandomizerUtils.random(String.class),
                "description", RandomizerUtils.random(String.class)
        );
        try {
            return JsonMapper.builder().build().writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static ReportRandomizer builder() {
        return new ReportRandomizer();
    }

    public static Report random() {
        return builder().build();
    }

    public Report build() {
        return new Report(
                type,
                status,
                createdAt,
                metadata
        );
    }
}

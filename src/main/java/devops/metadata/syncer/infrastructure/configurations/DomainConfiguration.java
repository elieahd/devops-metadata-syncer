package devops.metadata.syncer.infrastructure.configurations;

import devops.metadata.syncer.DevOpsMetadataSyncer;
import devops.metadata.syncer.domain.services.DomainService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

@Configuration
@ComponentScan(
        basePackageClasses = DevOpsMetadataSyncer.class,
        includeFilters = {
                @Filter(type = ANNOTATION, classes = DomainService.class)
        }
)
public class DomainConfiguration {
}

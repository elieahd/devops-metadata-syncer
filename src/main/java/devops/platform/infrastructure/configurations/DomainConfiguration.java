package devops.platform.infrastructure.configurations;

import devops.platform.DevOpsPlatformApplication;
import devops.platform.domain.services.DomainService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

@Configuration
@ComponentScan(
        basePackageClasses = DevOpsPlatformApplication.class,
        includeFilters = {
                @Filter(type = ANNOTATION, classes = DomainService.class)
        }
)
public class DomainConfiguration {
}

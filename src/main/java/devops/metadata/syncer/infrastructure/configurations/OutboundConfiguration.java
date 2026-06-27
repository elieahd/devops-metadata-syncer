package devops.metadata.syncer.infrastructure.configurations;

import devops.metadata.syncer.DevOpsMetadataSyncer;
import devops.metadata.syncer.infrastructure.outbound.OutboundAdapter;
import devops.metadata.syncer.infrastructure.outbound.github.GitHubClient;
import devops.metadata.syncer.infrastructure.outbound.github.GitHubClientAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

@Configuration
@ComponentScan(
        basePackageClasses = DevOpsMetadataSyncer.class,
        includeFilters = {
                @Filter(type = ANNOTATION, classes = OutboundAdapter.class)
        }
)
public class OutboundConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    @Bean("githubClient")
    public GitHubClient githubClient(@Value("${source.github.url}") String githubUrl,
                                     @Value("${source.github.token}") String githubToken) {
        return new GitHubClientAdapter(githubUrl, githubToken);
    }

    @Bean("enterpriseGithubClient")
    public GitHubClient enterprseGithubClient(@Value("${source.enterprise-github.url}") String githubUrl,
                                              @Value("${source.enterprise-github.token}") String githubToken) {
        return new GitHubClientAdapter(githubUrl, githubToken);
    }
}

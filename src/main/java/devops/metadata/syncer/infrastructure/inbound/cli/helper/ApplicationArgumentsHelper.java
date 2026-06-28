package devops.metadata.syncer.infrastructure.inbound.cli.helper;

import org.springframework.boot.ApplicationArguments;

import java.util.List;
import java.util.Optional;

public class ApplicationArgumentsHelper {

    private final ApplicationArguments args;

    private ApplicationArgumentsHelper(ApplicationArguments args) {
        this.args = args;
    }

    public static ApplicationArgumentsHelper of(ApplicationArguments args) {
        return new ApplicationArgumentsHelper(args);
    }

    public Optional<String> getArg(String argName) {
        if (!args.containsOption(argName)) {
            return Optional.empty();
        }

        List<String> values = args.getOptionValues(argName);

        if (values == null || values.isEmpty()) {
            return Optional.empty();
        }

        String firstValue = values.getFirst();

        return Optional.ofNullable(firstValue);
    }

}
package devops.metadata.syncer.infrastructure.inbound.cli.helper;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.ApplicationArguments;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApplicationArgumentsStub implements ApplicationArguments {

    private final Map<String, List<String>> options;

    public ApplicationArgumentsStub(Map<String, List<String>> options) {
        this.options = options;
    }

    @Override
    public String[] getSourceArgs() {
        return options.keySet().toArray(new String[0]);
    }

    @Override
    public Set<String> getOptionNames() {
        return options.keySet();
    }

    @Override
    public boolean containsOption(String name) {
        return options.containsKey(name);
    }

    @Override
    public @Nullable List<String> getOptionValues(String name) {
        return options.get(name);
    }

    @Override
    public List<String> getNonOptionArgs() {
        return List.of();
    }
}

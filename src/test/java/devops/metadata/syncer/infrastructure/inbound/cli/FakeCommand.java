package devops.metadata.syncer.infrastructure.inbound.cli;

import org.springframework.boot.ApplicationArguments;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class FakeCommand implements Command {
    private final String name;
    private final AtomicBoolean executed = new AtomicBoolean(false);
    private final AtomicReference<ApplicationArguments> receivedArgs = new AtomicReference<>();

    public FakeCommand(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String usage() {
        return "usage";
    }

    @Override
    public void execute(ApplicationArguments args) {
        executed.set(true);
        receivedArgs.set(args);
    }

    public boolean wasExecuted() {
        return executed.get();
    }

    public ApplicationArguments getReceivedArgs() {
        return receivedArgs.get();
    }
}

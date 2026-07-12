package devops.platform.infrastructure.outbound.synchronizers.github.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface Mapper {

    default <I, O> List<O> map(List<I> input,
                               Function<I, O> mapper) {

        if (input == null || input.isEmpty()) {
            return new ArrayList<>();
        }

        return input.stream().map(mapper).toList();
    }

}

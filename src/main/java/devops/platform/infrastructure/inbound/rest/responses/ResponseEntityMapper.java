package devops.platform.infrastructure.inbound.rest.responses;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.function.Function;

public class ResponseEntityMapper {

    private ResponseEntityMapper() {
        // utility class shouldn't be instantiated'
    }

    public static <I, O> ResponseEntity<List<O>> ok(List<I> input,
                                                    Function<I, O> mapper) {
        if (input == null || input.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<O> output = input.stream().map(mapper).toList();
        return ResponseEntity.ok(output);
    }

}

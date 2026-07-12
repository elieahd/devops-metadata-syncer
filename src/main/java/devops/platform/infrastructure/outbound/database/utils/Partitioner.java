package devops.platform.infrastructure.outbound.database.utils;

import java.util.ArrayList;
import java.util.List;

public class Partitioner {

    private Partitioner() {
        // utility class shouldn't be instantiated
    }

    public static <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }
}

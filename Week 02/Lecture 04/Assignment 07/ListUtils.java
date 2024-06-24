import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtils {
    public static <T> List<T> removeDuplicates(List<T> list) {
        return list.stream()
                .distinct() // Filter out duplicates
                .collect(Collectors.toList());
    }

    public static <T, K> List<T> removeDuplicatesByField(List<T> list, Function<? super T, ? extends K> keyExtractor) {
        Set<K> seenKeys = new HashSet<>();
        return list.stream()
                .filter(e -> seenKeys.add(keyExtractor.apply(e))) // used to track the keys that have already been encountered.
                .collect(Collectors.toList());
    }

    public static <T, U extends Comparable<? super U>> List<T> sortByField(List<T> list, Function<? super T, ? extends U> keyExtractor) {
        return list.stream()
                .sorted(Comparator.comparing(keyExtractor))
                .collect(Collectors.toList());
    }

    public static <T, U extends Comparable<? super U>> Optional<T> findMaxByField(List<T> list, Function<? super T, ? extends U> keyExtractor) {
        return list.stream()
                .max(Comparator.comparing(keyExtractor));
    }

    public static <T, K> Map<K, T> convertListToMap(List<T> list, Function<? super T, ? extends K> keyExtractor) {
        return list.stream()
                .filter(e -> keyExtractor.apply(e) != null) // Exclude elements with null keys
                .collect(Collectors.toMap(
                        keyExtractor,
                        Function.identity(),
                        (existing, replacement) -> replacement // Replace existing with new in case of duplicates
                ));
    }
}
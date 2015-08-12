package ch.codebulb.lambdaomega.abstractions;

import static ch.codebulb.lambdaomega.F.compare;
import static ch.codebulb.lambdaomega.F.compareAsc;
import static ch.codebulb.lambdaomega.L.L;
import ch.codebulb.lambdaomega.M;
import ch.codebulb.lambdaomega.V2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Adds a contract to work with functional interfaces to a {@link SequentialIAdd}.
 */
public interface SequentialIFunctions<T> extends SequentialIAdd<T>, FunctionsI {
    public default String join(CharSequence delimiter) {
        return stream().map(it -> it.toString()).collect(Collectors.joining(delimiter));
    }
    
    public default void forEach(Consumer<? super T> function) {
        stream().forEach(function);
    }
    
    public default <R> Collection<R> map(Function<T, R> function) {
        return stream().map(function).collect(createCollector());
    }
    
    public default T reduce(T identity, BinaryOperator<T> accumulator) {
        return stream().reduce(identity, accumulator);
    }
    
    public default <RK, RV> Map<RK, RV> mapEntries(Function<T, M.E<RK, RV>> function) {
        return stream().map(function).collect(Collectors.toMap(it -> it.k, it -> it.v));
    }
    
    public default <N> List<N> flatten() {
        return (List<N>)(flatten(stream()).collect(Collectors.toList()));
    }
    
    public static <N, T> Stream<N> flatten(Stream<T> stream) {
        return stream.flatMap(it -> {
            if (it instanceof SequentialI) {
                return ((SequentialI<N>)it).stream();
            }
            else if (it instanceof Collection) {
                return ((Collection<N>)it).stream();
            }
            else {
                return Stream.of((N)(it));
            }
        });
    }
    
    public default <N> List<N> flattenDeep() {
        // TODO This seems a bit shaky. Rewrite.
        Collection<?> ret = toCollection();
        while (ret.stream().anyMatch(it -> it instanceof SequentialI || it instanceof Collection)) {
            ret = ((Stream<?>)ret.stream()).flatMap(it -> {
                if (it instanceof SequentialI) {
                    return ((SequentialI<N>)it).stream();
                }
                else if (it instanceof Collection) {
                    return ((Collection<N>)it).stream();
                }
                else {
                    return Stream.of((N)(it));
                }
            }).collect(Collectors.toList());
        }
        return ((Stream<N>)(ret.stream())).collect(Collectors.toList());
    }
    
    public default T find(Predicate<T> predicate) {
        Optional<T> found = stream().filter(predicate).findFirst();
        if (found.isPresent()) {
            return found.get();
        }
        else {
            return null;
        }
    }
    
    public default Collection<T> findAll(Predicate<T> predicate) {
        return stream().filter(predicate).collect(createCollector());
    }
    
    public default Collection<T> filter(Predicate<T> predicate) {
        return findAll(predicate);
    }
    
    public default Collection<T> reject(Predicate<T> predicate) {
        return findAll(predicate.negate());
    }
    
    public default List<T> sortAscBy(Function<T, Comparable>... keyExtractors) {
        return stream().sorted(compareAsc(keyExtractors)).collect(Collectors.toList());
    }
    
    public default List<T> sortDescBy(Function<T, Comparable>... keyExtractors) {
        return stream().sorted(compareAsc(keyExtractors).reversed()).collect(Collectors.toList());
    }
    
    public default List<T> sortBy(Function<T, V2<Function<? super T, Comparable>, Boolean>>... keyExtractors) {
        return stream().sorted(compare(keyExtractors)).collect(Collectors.toList());
    }
    
    public default List<T> shuffle() {
        List<T> ret = new ArrayList(toCollection());
        Collections.shuffle(ret);
        return ret;
    }
    
    public default T min(Function<T, Comparable>... keyExtractors) {
        return Collections.min(toCollection(), compareAsc(keyExtractors));
    }
    
    public default T max(Function<T, Comparable>... keyExtractors) {
        return Collections.max(toCollection(), compareAsc(keyExtractors));
    }
    
    public default int count(Predicate<T> predicate) {
        return findAll(predicate).size();
    }
    
    public default double sum(ToDoubleFunction<? super T> mapper) {
        return stream().collect(Collectors.summingDouble(mapper));
    }
    
    public default <K> Map<K, ? extends Collection<T>> groupBy(Function<? super T, ? extends K> classifier) {
        return stream().collect(Collectors.groupingBy(classifier, createCollector()));
    }
    
    public default Map<Boolean, ? extends Collection<T>> partition(Predicate<? super T> predicate) {
        return stream().collect(Collectors.groupingBy(it -> predicate.test(it), createCollector()));
    }
    
    public default boolean every(Predicate<? super T> predicate) {
        return stream().allMatch(predicate);
    }
    
    public default boolean some(Predicate<? super T> predicate) {
        return stream().anyMatch(predicate);
    }
    
    public default boolean none(Predicate<? super T> predicate) {
        return stream().noneMatch(predicate);
    }
}

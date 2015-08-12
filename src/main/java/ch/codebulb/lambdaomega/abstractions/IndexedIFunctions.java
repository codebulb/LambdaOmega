package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import static ch.codebulb.lambdaomega.F.*;
import ch.codebulb.lambdaomega.M;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;

/**
 * Adds a contract to work with functional interfaces to a {@link IndexedI}.
 */
public interface IndexedIFunctions<K, V> extends IndexedI<K, V>, FunctionsI {
    public default void forEach(BiConsumer<K, V> action) {
        getEntries().forEach(consumer(action));
    }
    
    public default <R> Collection<R> map(BiFunction<K, V, R> function) {
        return getEntries().stream().map(function(function)).collect(createCollector());
    }
    
    public default <RK, RV> Map<RK, RV> mapEntries(BiFunction<K, V, M.E<RK, RV>> function) {
        return getEntries().stream().map(function(function)).collect(Collectors.toMap(it -> it.k, it -> it.v));
    }

    public default M.E<K, V> find(BiPredicate<K, V> predicate) {
        Optional<M.E<K, V>> found = getEntries().stream().filter(predicate(predicate)).findFirst();
        if (found.isPresent()) {
            return found.get();
        }
        else {
            return null;
        }
    }
    
    public default Map<K, V> findAll(BiPredicate<K, V> predicate) {
        return getEntries().stream().filter(predicate(predicate)).collect(Collectors.toMap(it -> it.k, it -> it.v));
    }
    
    public default Map<K, V> filter(BiPredicate<K, V> predicate) {
        return findAll(predicate);
    }
    
    public default Map<K, V> reject(BiPredicate<K, V> predicate) {
        return findAll(predicate.negate());
    }
    
    public default M.E<K, V> min(BiFunction<K, V, Comparable>... keyExtractors) {
        return Collections.min(getEntries(), compareAsc(C.map(keyExtractors, it -> function(it))));
    }
    
    public default M.E<K, V> max(BiFunction<K, V, Comparable>... keyExtractors) {
        return Collections.max(getEntries(), compareAsc(C.map(keyExtractors, it -> function(it))));
    }
    
    public default int count(BiPredicate<K, V> predicate) {
        return findAll(predicate).size();
    }
    
    public default double sum(ToDoubleBiFunction<? super K, ? super V> mapper) {
        return getEntries().stream().collect(Collectors.summingDouble(toDoubleFunction(mapper)));
    }
    
    public default boolean every(BiPredicate<K, V> predicate) {
        return getEntries().stream().allMatch(predicate(predicate));
    }
    
    public default boolean some(BiPredicate<K, V> predicate) {
        return getEntries().stream().anyMatch(predicate(predicate));
    }
    
    public default boolean none(BiPredicate<K, V> predicate) {
        return getEntries().stream().noneMatch(predicate(predicate));
    }
}

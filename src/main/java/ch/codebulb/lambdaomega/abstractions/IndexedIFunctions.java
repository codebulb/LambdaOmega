package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import static ch.codebulb.lambdaomega.F.*;
import ch.codebulb.lambdaomega.M;
import ch.codebulb.lambdaomega.M.E;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;

/**
 * Adds a contract to work with functional interfaces to a {@link IndexedI}. None of these functions alter the original wrapped collection.
 */
public interface IndexedIFunctions<K, V> extends IndexedI<K, V>, FunctionsI {
    /**
     * 
     * @see Map#forEach(BiConsumer)
     */
    public default void forEach(BiConsumer<K, V> action) {
        getEntries().forEach(consumer(action));
    }
    
    /**
     * Collects the result of a map with a function <i>f: (K, V) &rarr; R</i> as a {@link List}.
     */
    public default <R> Collection<R> map(BiFunction<K, V, R> function) {
        return getEntries().stream().map(function(function)).collect(createCollector());
    }
    
    /**
     * Collects the result of a map with a function <i>f: (K, V) &rarr; {@link E}&lt;K, V&gt;</i> as a {@link Map}, built from the returned {@link E}s.
     */
    public default <RK, RV> Map<RK, RV> mapEntries(BiFunction<K, V, M.E<RK, RV>> function) {
        return getEntries().stream().map(function(function)).collect(Collectors.toMap(it -> it.k, it -> it.v));
    }

    /**
     * Returns the first element for with the <code>predicate</code> provided returns <code>true</code>,
     * or <code>null</code> if no such element is found.
     */
    public default M.E<K, V> find(BiPredicate<K, V> predicate) {
        Optional<M.E<K, V>> found = getEntries().stream().filter(predicate(predicate)).findFirst();
        if (found.isPresent()) {
            return found.get();
        }
        else {
            return null;
        }
    }
    
    /**
     * Returns a {@link Map} with every element for with the <code>predicate</code> provided returns <code>true</code>.
     * This is the opposite of {@link #reject(BiPredicate)}.
     */
    public default Map<K, V> findAll(BiPredicate<K, V> predicate) {
        return getEntries().stream().filter(predicate(predicate)).collect(Collectors.toMap(it -> it.k, it -> it.v));
    }
    
    /**
     * @see #findAll(BiPredicate)
     */
    public default Map<K, V> filter(BiPredicate<K, V> predicate) {
        return findAll(predicate);
    }
    
    /**
     * Returns a {@link Map} with every element for with the <code>predicate</code> provided returns <code>false</code>.
     * This is the opposite of {@link #findAll(BiPredicate)} / {@link #filter(BiPredicate)}.
     */
    public default Map<K, V> reject(BiPredicate<K, V> predicate) {
        return findAll(predicate.negate());
    }
    
    /**
     * Returns the element with the minimum value after invoking the <code>keyExtractors</code> provided in order as a comparator
     * on every element.
     */
    public default M.E<K, V> min(BiFunction<K, V, Comparable>... keyExtractors) {
        return Collections.min(getEntries(), compareAsc(C.map(keyExtractors, it -> function(it))));
    }
    
    /**
     * Returns the element with the maximum value after invoking the <code>keyExtractors</code> provided in order as a comparator
     * on every element.
     */
    public default M.E<K, V> max(BiFunction<K, V, Comparable>... keyExtractors) {
        return Collections.max(getEntries(), compareAsc(C.map(keyExtractors, it -> function(it))));
    }
    
    /**
     * Returns the number of elements for which the <code>predicate</code> provided returns <code>true</code>.
     */
    public default int count(BiPredicate<K, V> predicate) {
        return findAll(predicate).size();
    }
    
    /**
     * Returns the sum of elements if the int-<code>mapper</code> provided is applied on every element.
     */
    public default double sum(ToDoubleBiFunction<? super K, ? super V> mapper) {
        return getEntries().stream().collect(Collectors.summingDouble(toDoubleFunction(mapper)));
    }
    
    /**
     * Returns <code>true</code>, if the <code>predicate</code> provided returns <code>true</code> for every element.
     */
    public default boolean every(BiPredicate<K, V> predicate) {
        return getEntries().stream().allMatch(predicate(predicate));
    }
    
    /**
     * Returns <code>true</code>, if the <code>predicate</code> provided returns <code>true</code> for at least one element.
     */
    public default boolean some(BiPredicate<K, V> predicate) {
        return getEntries().stream().anyMatch(predicate(predicate));
    }
    
    /**
     * Returns <code>true</code>, if the <code>predicate</code> provided returns <code>true</code> for none of the elements.
     */
    public default boolean none(BiPredicate<K, V> predicate) {
        return getEntries().stream().noneMatch(predicate(predicate));
    }
}

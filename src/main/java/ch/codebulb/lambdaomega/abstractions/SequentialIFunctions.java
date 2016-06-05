package ch.codebulb.lambdaomega.abstractions;

import static ch.codebulb.lambdaomega.F.compare;
import static ch.codebulb.lambdaomega.F.compareAsc;
import ch.codebulb.lambdaomega.M;
import ch.codebulb.lambdaomega.M.E;
import ch.codebulb.lambdaomega.V2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Adds a contract to work with functional interfaces to a {@link SequentialI}. None of these functions alter the original wrapped collection.
 */
public interface SequentialIFunctions<T> extends SequentialI<T>, FunctionsI {
    /**
     * Joins all elements into a String with the <code>delimiter</code> provided.
     */
    public default String join(CharSequence delimiter) {
        return stream().map(it -> it.toString()).collect(Collectors.joining(delimiter));
    }
    
    /**
     * @see Stream#forEach(Consumer)
     */
    public default void forEach(Consumer<? super T> function) {
        stream().forEach(function);
    }
    
    /**
     * Collects the result of a map with a function <i>f: (T) &rarr; R</i> as a {@link List}.
     */
    public default <R> Collection<R> map(Function<T, R> function) {
        return stream().map(function).collect(createCollector());
    }
    
    /**
     * Performs a "reduce" function with a single result <i>f: (T, A) &rarr; R</i> on the elements using an accumulator,
     * injecting an <code>identity</code> element as the first accumulator value.
     * This is the second step of the "map-reduce" algorithm. Other functions such as {@link #min(Function...)} and
     * {@link #sum(ToDoubleFunction)} could actually be re-implemented using this function.
     */
    public default T reduce(T identity, BinaryOperator<T> accumulator) {
        return stream().reduce(identity, accumulator);
    }
    
    /**
     * Collects the result of a map with a function <i>f: (T) &rarr; {@link E}&lt;K, V&gt;</i> as a {@link Map}, built from the returned {@link E}s.
     */
    public default <RK, RV> Map<RK, RV> mapEntries(Function<T, M.E<RK, RV>> function) {
        return stream().map(function).collect(Collectors.toMap(it -> it.k, it -> it.v));
    }
    
    /**
     * Returns a transformation of a list of one depth level into a flat list.
     * 
     * @see #flattenDeep()
     */
    public default <N> Collection<N> flatten() {
        return (Collection<N>)(flatten(stream()).collect(createCollector()));
    }
    
    static <N, T> Stream<N> flatten(Stream<T> stream) {
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
    
    /**
     * Returns a transformation of a list of an arbitrary depth level into a flat list.
     * 
     * @see #flatten()
     */
    public default <N> Collection<N> flattenDeep() {
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
            }).collect(createCollector());
        }
        return ((Stream<N>)(ret.stream())).collect(createCollector());
    }
    
    /**
     * Returns the first element for with the <code>predicate</code> provided returns <code>true</code>,
     * or <code>null</code> if no such element is found.
     */
    public default T find(Predicate<T> predicate) {
        Optional<T> found = stream().filter(predicate).findFirst();
        if (found.isPresent()) {
            return found.get();
        }
        else {
            return null;
        }
    }
    
    /**
     * Returns a {@link Collection} with every element for with the <code>predicate</code> provided returns <code>true</code>.
     * This is the opposite of {@link #reject(Predicate)}.
     */
    public default Collection<T> findAll(Predicate<T> predicate) {
        return stream().filter(predicate).collect(createCollector());
    }
    
    /**
     * @see #findAll(Predicate)
     */
    public default Collection<T> filter(Predicate<T> predicate) {
        return findAll(predicate);
    }
    
    /**
     * Returns a {@link Map} with every element for with the <code>predicate</code> provided returns <code>false</code>.
     * This is the opposite of {@link #findAll(Predicate)} / {@link #filter(Predicate)}.
     */
    public default Collection<T> reject(Predicate<T> predicate) {
        return findAll(predicate.negate());
    }
    
    /**
     * Returns a {@link List} where the elements are sorted <i>in ascending order</i> using the <code>keyExtractors</code> provided in order
     * as a comparator on every element.
     * 
     * @see {@link #sortDescBy(Function)}, {@link #sortBy(Function...)}
     */
    public default List<T> sortAscBy(Function<T, Comparable>... keyExtractors) {
        return stream().sorted(compareAsc(keyExtractors)).collect(Collectors.toList());
    }
    
    /**
     * Returns a {@link List} where the elements are sorted <i>in descending order</i> using the <code>keyExtractors</code> provided in order
     * as a comparator on every element.
     * 
     * @see {@link #sortAscBy(Function...)}, {@link #sortBy(Function...)}
     */
    public default List<T> sortDescBy(Function<T, Comparable>... keyExtractors) {
        return stream().sorted(compareAsc(keyExtractors).reversed()).collect(Collectors.toList());
    }
    
    /**
     * Returns a {@link List} where the elements are sorted using the <code>keyExtractors</code> provided in order
     * as a comparator on every element. Every keyExtractor is a function <i>f: (T) &rarr; {@link V2}&lt;f1, Boolean&gt;</i> where the return type is 
     * a {@link V2}; its 1st element is the actual keyExtractor function <i>f1</i>, its 2nd element is a Boolean specifying the sort order for that specific
     * keyExtractor: <code>true</code> for <i>ascending</i>.
     * 
     * @see {@link #sortAscBy(Function...)}, {@link #sortBy(Function...)}
     */
    public default List<T> sortBy(Function<T, V2<Function<? super T, Comparable>, Boolean>>... keyExtractors) {
        return stream().sorted(compare(keyExtractors)).collect(Collectors.toList());
    }
    
    /**
     * Returns a randomly shuffled {@link List} of all elements.
     * 
     * @see Collections#shuffle(List)
     */
    public default List<T> shuffle() {
        List<T> ret = new ArrayList(toCollection());
        Collections.shuffle(ret);
        return ret;
    }
    
    /**
     * Returns the element with the minimum value after invoking the <code>keyExtractors</code> provided in order as a comparator
     * on every element.
     */
    public default T min(Function<T, Comparable>... keyExtractors) {
        return Collections.min(toCollection(), compareAsc(keyExtractors));
    }
    
    /**
     * Returns the element with the maximum value after invoking the <code>keyExtractors</code> provided in order as a comparator
     * on every element.
     */
    public default T max(Function<T, Comparable>... keyExtractors) {
        return Collections.max(toCollection(), compareAsc(keyExtractors));
    }
    
    /**
     * Returns the number of elements for which the <code>predicate</code> provided returns <code>true</code>.
     */
    public default int count(Predicate<T> predicate) {
        return findAll(predicate).size();
    }
    
    /**
     * Returns the sum of elements if the int-<code>mapper</code> provided is applied on every element.
     */
    public default double sum(ToDoubleFunction<? super T> mapper) {
        return stream().collect(Collectors.summingDouble(mapper));
    }
    
    /**
     * Returns a {@link Map} which groups the elements using the <code>classifier</code> provided.
     */
    public default <K> Map<K, ? extends Collection<T>> groupBy(Function<? super T, ? extends K> classifier) {
        return stream().collect(Collectors.groupingBy(classifier, createCollector()));
    }
    
    /**
     * Like {@link #groupBy(Function)}, but there are exactly two groups: one for elements for which 
     * the <code>predicate</code> returns <code>true</code>, and one for which it returns <code>false</code>
     */
    public default Map<Boolean, ? extends Collection<T>> partition(Predicate<? super T> predicate) {
        return stream().collect(Collectors.groupingBy(it -> predicate.test(it), createCollector()));
    }
    
    /**
     * Returns <code>true</code>, if the <code>predicate</code> provided returns <code>true</code> for every element.
     */
    public default boolean every(Predicate<? super T> predicate) {
        return stream().allMatch(predicate);
    }
    
    /**
     * Returns <code>true</code>, if the <code>predicate</code> provided returns <code>true</code> for at least one element.
     */
    public default boolean some(Predicate<? super T> predicate) {
        return stream().anyMatch(predicate);
    }
    
    /**
     * Returns <code>true</code>, if the <code>predicate</code> provided returns <code>true</code> for none of the elements.
     */
    public default boolean none(Predicate<? super T> predicate) {
        return stream().noneMatch(predicate);
    }
}

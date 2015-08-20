package ch.codebulb.lambdaomega.abstractions;

import static ch.codebulb.lambdaomega.L.L;
import ch.codebulb.lambdaomega.L;
import ch.codebulb.lambdaomega.M;
import static ch.codebulb.lambdaomega.M.m;
import ch.codebulb.lambdaomega.V2;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Adds syntactic sugar to a {@link SequentialIFunctions}.
 */
public interface SequentialIFunctionsS<T> extends SequentialIFunctions<T> {
    /**
     * @see #map(Function)
     */
    public default <R> L<R> Map(Function<T, R> function) {
        return L(map(function));
    }
    
    /**
     * @see #mapEntries(Function)
     */
    public default <RK, RV> M<RK, RV> MapEntries(Function<T, M.E<RK, RV>> function) {
        return m(mapEntries(function));
    }
    
    /**
     * @see #flatten()
     */
    default <N> L<N> Flatten() {
        return L(flatten());
    }
    
    /**
     * @see #flattenDeep()
     */
    public default <N> L<N> FlattenDeep() {
        return L(flattenDeep());
    }
    
    /**
     * @see #findAll(Predicate)
     */
    public default L<T> FindAll(Predicate<T> predicate) {
        return L(findAll(predicate));
    }
    
    /**
     * @see #filter(Predicate)
     */
    public default L<T> Filter(Predicate<T> predicate) {
        return L(filter(predicate));
    }
    
    /**
     * @see #reject(Predicate)
     */
    public default L<T> Reject(Predicate<T> predicate) {
        return L(reject(predicate));
    }
    
    /**
     * @see #sortAscBy(Function...)
     */
    public default L<T> SortAscBy(Function<T, Comparable>... keyExtractors) {
        return L(sortAscBy(keyExtractors));
    }
    
    /**
     * @see #sortDescBy(Function...)
     */
    public default L<T> SortDescBy(Function<T, Comparable>... keyExtractors) {
        return L(sortDescBy(keyExtractors));
    }
    
    /**
     * @see #sortBy(Function...)
     */
    public default L<T> SortBy(Function<T, V2<Function<? super T, Comparable>, Boolean>>... keyExtractors) {
        return L(sortBy(keyExtractors));
    }
    
    /**
     * @see #groupBy(Function)
     */
    public default <K> M<K, ? extends Collection<T>> GroupBy(Function<? super T, ? extends K> classifier) {
        return m(groupBy(classifier));
    }
    
    /**
     * @see #partition(Predicate)
     */
    public default M<Boolean, ? extends Collection<T>> Partition(Predicate<? super T> predicate) {
        return m(partition(predicate));
    }
}

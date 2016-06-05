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
    public <R> SequentialIFunctionsS<R> Map(Function<T, R> function);
    
    /**
     * @see #mapEntries(Function)
     */
    public default <RK, RV> M<RK, RV> MapEntries(Function<T, M.E<RK, RV>> function) {
        return M.m(mapEntries(function));
    }
    
    /**
     * @see #flatten()
     */
    public <N> SequentialIFunctionsS<N> Flatten();
    
    /**
     * @see #flattenDeep()
     */
    public <N> SequentialIFunctionsS<N> FlattenDeep();
    
    /**
     * @see #findAll(Predicate)
     */
    public SequentialIFunctionsS<T> FindAll(Predicate<T> predicate);
    
    /**
     * @see #filter(Predicate)
     */
    public SequentialIFunctionsS<T> Filter(Predicate<T> predicate);
    
    /**
     * @see #reject(Predicate)
     */
    public SequentialIFunctionsS<T> Reject(Predicate<T> predicate);
    
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

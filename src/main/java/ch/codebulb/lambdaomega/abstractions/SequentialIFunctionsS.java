package ch.codebulb.lambdaomega.abstractions;

import static ch.codebulb.lambdaomega.L.L;
import ch.codebulb.lambdaomega.L;
import ch.codebulb.lambdaomega.M;
import static ch.codebulb.lambdaomega.M.m;
import ch.codebulb.lambdaomega.V2;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Adds syntactic sugar to a {@link SequentialIFunctions}.
 */
public interface SequentialIFunctionsS<T> extends SequentialIFunctions<T> {
    public default <R> L<R> Map(Function<T, R> function) {
        return L(map(function));
    }
    
    public default <RK, RV> M<RK, RV> MapEntries(Function<T, M.E<RK, RV>> function) {
        return m(mapEntries(function));
    }
    
    default <N> L<N> Flatten() {
        return L(flatten());
    }
    
    public default <N> L<N> FlattenDeep() {
        return L(flattenDeep());
    }
    
    public default L<T> FindAll(Predicate<T> predicate) {
        return L(findAll(predicate));
    }
    
    public default L<T> Filter(Predicate<T> predicate) {
        return L(filter(predicate));
    }
    
    public default L<T> Reject(Predicate<T> predicate) {
        return L(reject(predicate));
    }
    
    public default L<T> SortAscBy(Function<T, Comparable>... keyExtractors) {
        return L(sortAscBy(keyExtractors));
    }
    
    public default L<T> SortDescBy(Function<T, Comparable>... keyExtractors) {
        return L(sortDescBy(keyExtractors));
    }
    
    public default L<T> SortBy(Function<T, V2<Function<? super T, Comparable>, Boolean>>... keyExtractors) {
        return L(sortBy(keyExtractors));
    }
    
    public default <K> M<K, ? extends Collection<T>> GroupBy(Function<? super T, ? extends K> classifier) {
        return m(groupBy(classifier));
    }
    
    public default M<Boolean, ? extends Collection<T>> Partition(Predicate<? super T> predicate) {
        return m(partition(predicate));
    }
}

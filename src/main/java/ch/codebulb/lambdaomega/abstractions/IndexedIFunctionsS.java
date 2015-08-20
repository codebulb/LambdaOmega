package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.L;
import static ch.codebulb.lambdaomega.L.L;
import ch.codebulb.lambdaomega.M;
import static ch.codebulb.lambdaomega.M.m;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * Adds syntactic sugar to a {@link IndexedIFunctions}.
 */
public interface IndexedIFunctionsS<K, V> extends IndexedIFunctions<K, V> {
    /**
     * @see #map(BiFunction)
     */
    public default <R> L<R> Map(BiFunction<K, V, R> function) {
        return L(map(function));
    }
    
    /**
     * @see #mapEntries(BiFunction)
     */
    public default <RK, RV> M<RK, RV> MapEntries(BiFunction<K, V, M.E<RK, RV>> function) {
        return m(mapEntries(function));
    }
    
    /**
     * @see #findAll(BiPredicate)
     */
    public default M<K, V> FindAll(BiPredicate<K, V> predicate) {
        return m(findAll(predicate));
    }
    
    /**
     * @see #filter(BiPredicate)
     */
    public default M<K, V> Filter(BiPredicate<K, V> predicate) {
        return m(filter(predicate));
    }
    
    /**
     * @see #reject(BiPredicate)
     */
    public default M<K, V> Reject(BiPredicate<K, V> predicate) {
        return m(reject(predicate));
    }
}

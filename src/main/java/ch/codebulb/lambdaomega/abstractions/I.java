package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import ch.codebulb.lambdaomega.L;
import static ch.codebulb.lambdaomega.L.L;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This interface type can be used as a convenience shorthand alias for {@link ReadonlyIndexedI}.
 */
public interface I<K, V> {
    // We have to allow derived return values because we need that in V2
    /**
     * Invokes Map#get(Object) or invokes the function set by {@link #WithDefault(Function)} with the <code>key</code> provided,
     * if it would return <code>null</code> or the key is invalid.
     */
    public <VN extends V> VN get(K key);
    
    /**
     * @see #get(Object)
     */
    public default List<V> get(K... keys) {
        return C.toStream(keys).map(it -> this.<V> get(it)).collect(Collectors.toList());
    }
    
    /**
     * @see #get(Object)
     */
    public default List<V> getAll(Collection<K>... keys) {
        return C.toStream(keys).map(c -> C.toStream(c).map(it -> this.<V> get(it)).collect(Collectors.toList()))
                .flatMap(Collection::stream).collect(Collectors.toList());
    }
    
    // We have to allow derived return values because we need that in V2
    /**
     * @see Map#getOrDefault(Object, Object)
     */
    public <VN extends V> VN getOrDefault(K key, VN defaultValue);
    
    /**
     * Sets the function for creating a default value as an alternative to an otherwise unsuccessful {@link #get(Object)} invocation.
     * 
     * @param defaultValue the new default value
     * @return <code>this (modified)</code>
     */
    public ReadonlyIndexedI<K, V> WithDefault(Function<K, V> defaultValue);
    
    /**
     * @see #get(Object)
     */
    public default L<V> g(K... keys) {
        return Get(keys);
    }
    
    /**
     * @see #get(Object)
     */
    public default L<V> Get(K... keys) {
        return L(get(keys));
    }
    
    /**
     * @see #get(Object)
     */
    public default L<V> GetAll(Collection<K>... keys) {
        return L(getAll(keys));
    }
    
    /**
     * @see #get(Object)
     */
    public default L<V> G(Collection<K>... keys) {
        return L(getAll(keys));
    }
}

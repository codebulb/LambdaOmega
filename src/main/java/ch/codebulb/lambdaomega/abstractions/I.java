package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import ch.codebulb.lambdaomega.L;
import static ch.codebulb.lambdaomega.L.L;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This interface type can be used as a convenience shorthand alias for {@link ReadonlyIndexedI}.
 */
public interface I<K, V> {
    // We have to allow derived return values because we need that in V2
    public <VN extends V> VN get(K key);
    
    public default List<V> get(K... keys) {
        return C.toStream(keys).map(it -> this.<V> get(it)).collect(Collectors.toList());
    }
    
    public default List<V> getAll(Collection<K>... keys) {
        return C.toStream(keys).map(c -> C.toStream(c).map(it -> this.<V> get(it)).collect(Collectors.toList()))
                .flatMap(Collection::stream).collect(Collectors.toList());
    }
    
    public <VN extends V> VN getOrDefault(K key, V defaultValue);
    
    public ReadonlyIndexedI<K, V> WithDefault(Function<K, V> defaultValue);
    
    public default <VN extends V> VN g(K key) {
        return get(key);
    }
    
    public default L<V> g(K... keys) {
        return Get(keys);
    }
    
    public default L<V> Get(K... keys) {
        return L(get(keys));
    }
    
    public default L<V> GetAll(Collection<K>... keys) {
        return L(getAll(keys));
    }
    
    public default L<V> G(Collection<K>... keys) {
        return L(getAll(keys));
    }
}

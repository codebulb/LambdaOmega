package ch.codebulb.lambdaomega.abstractions;

import java.util.Map;

/**
 * Adds syntactic sugar to a {@link IndexedListIAdd}.
 */
public interface IndexedListIS<K, V> extends IndexedListIAdd<K, V> {
    public default IndexedListI<K, V> Set(K index, V element) {
        set(index, element);
        return this;
    }
    
    public default IndexedListI<K, V> s(K index, V element) {
        return Set(index, element);
    }
    
    public default IndexedListI<K, V> SetAll(Map<? extends K, ? extends V>... m) {
        setAll(m);
        return this;
    }
    
    public default IndexedListI<K, V> S(Map<? extends K, ? extends V>... m) {
        return SetAll(m);
    }
    
    public default Map<K, V> setAll(IndexedI<? extends K, ? extends V>... m) {
        return putAll(m);
    }
    
    public default IndexedListI<K, V> SetAll(IndexedI<? extends K, ? extends V>... m) {
        setAll(m);
        return this;
    }
    
    public default IndexedListI<K, V> S(IndexedI<? extends K, ? extends V>... m) {
        return SetAll(m);
    }
}

package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import java.util.Collection;
import java.util.Map;

/**
 * Adds syntactic sugar to a {@link IndexedIFunctionsS}.
 */
public interface IndexedIS<K, V> extends IndexedIFunctionsS<K, V> {
    public default boolean containsAnyKey(Collection<K>... key) {
        return C.toStream(key).anyMatch(c -> C.toStream(c).anyMatch(it -> toMap().containsKey(it)));
    }
    
    public default boolean containsAnyValue(Collection<V>... value) {
        return C.toStream(value).anyMatch(c -> C.toStream(c).anyMatch(it -> containsAnyValue(it)));
    }
    
    public default IndexedI<K, V> Put(K key, V value) {
        put(key, value);
        return this;
    }
    
    public default IndexedI<K, V> p(K key, V value) {
        return Put(key, value);
    }
    
    public default IndexedI<K, V> PutAll(Map<? extends K, ? extends V>... m) {
        putAll(m);
        return this;
    }
    
    public default IndexedI<K, V> P(Map<? extends K, ? extends V>... m) {
        return PutAll(m);
    }
    
    public default Map<K, V> putAll(IndexedI<? extends K, ? extends V>... m) {
        return putAll(C.map(m, it -> it.toMap()));
    }
    
    public default IndexedI<K, V> PutAll(IndexedI<? extends K, ? extends V>... m) {
        putAll(m);
        return this;
    }
    
    public default IndexedI<K, V> P(IndexedI<? extends K, ? extends V>... m) {
        return PutAll(m);
    }
    
    public default IndexedI<K, V> DeleteKey(K... key) {
        deleteKey(key);
        return this;
    }
    
    public default IndexedI<K, V> d(K... key) {
        return DeleteKey(key);
    }
    
    public default IndexedI<K, V> DeleteAllKeys(Collection<? extends K>... keys) {
        deleteAllKeys(keys);
        return this;
    }
    
    public default IndexedI<K, V> D(Collection<? extends K>... keys) {
        return IndexedIS.this.DeleteAllKeys(keys);
    }
    
    public default Map<K, V> deleteAllKeys(SequentialI<? extends K>... keys) {
        C.toStream(keys).forEach(c -> C.toStream(c.toCollection()).forEach(it -> deleteKey(it)));
        return toMap();
    }
    
    public default IndexedI<K, V> DeleteAllKeys(SequentialI<? extends K>... keys) {
        deleteAllKeys(keys);
        return this;
    }
    
    public default IndexedI<K, V> D(SequentialI<? extends K>... keys) {
        return DeleteAllKeys(keys);
    }
    
    public default IndexedI<K, V> DeleteValue(V... value) {
        deleteValue(value);
        return this;
    }
    
    public default IndexedI<K, V> DeleteAllValues(Collection<? extends V>... values) {
        deleteAllValues(values);
        return this;
    }
    
    public default Map<K, V> deleteAllValues(SequentialI<? extends V>... values) {
        C.toStream(values).forEach(c -> C.toStream(c.toCollection()).forEach(it -> deleteValue(it)));
        return toMap();
    }
    
    public default IndexedI<K, V> DeleteAllValues(SequentialI<? extends V>... values) {
        deleteAllValues(values);
        return this;
    }
}

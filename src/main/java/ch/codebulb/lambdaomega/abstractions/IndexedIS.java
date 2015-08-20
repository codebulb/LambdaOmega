package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import java.util.Collection;
import java.util.Map;

/**
 * Adds syntactic sugar to a {@link IndexedIFunctionsS}.
 */
public interface IndexedIS<K, V> extends IndexedIFunctionsS<K, V> {
    /**
     * @see #containsAnyKey(Object...)
     */
    public default boolean containsAnyKey(Collection<K>... key) {
        return C.toStream(key).anyMatch(c -> C.toStream(c).anyMatch(it -> toMap().containsKey(it)));
    }
    
    /**
     * @see #containsAnyValue(Object...)
     */
    public default boolean containsAnyValue(Collection<V>... value) {
        return C.toStream(value).anyMatch(c -> C.toStream(c).anyMatch(it -> containsAnyValue(it)));
    }
    
    /**
     * @see #put(Object, Object)
     */
    public default IndexedI<K, V> Put(K key, V value) {
        put(key, value);
        return this;
    }
    
    /**
     * @see #put(Object, Object)
     */
    public default IndexedI<K, V> p(K key, V value) {
        return Put(key, value);
    }
    
    /**
     * @see #putAll(java.util.List)
     */
    public default IndexedI<K, V> PutAll(Map<? extends K, ? extends V>... m) {
        putAll(m);
        return this;
    }
    
    /**
     * @see #putAll(java.util.List)
     */
    public default IndexedI<K, V> P(Map<? extends K, ? extends V>... m) {
        return PutAll(m);
    }
    
    /**
     * @see #putAll(java.util.List)
     */
    public default Map<K, V> putAll(IndexedI<? extends K, ? extends V>... m) {
        return putAll(C.map(m, it -> it.toMap()));
    }
    
    /**
     * @see #putAll(java.util.List)
     */
    public default IndexedI<K, V> PutAll(IndexedI<? extends K, ? extends V>... m) {
        putAll(m);
        return this;
    }
    
    /**
     * @see #putAll(java.util.List)
     */
    public default IndexedI<K, V> P(IndexedI<? extends K, ? extends V>... m) {
        return PutAll(m);
    }
    
    /**
     * @see #deleteKey(Object...)
     */
    public default IndexedI<K, V> DeleteKey(K... key) {
        deleteKey(key);
        return this;
    }
    
    /**
     * @see #deleteKey(Object...)
     */
    public default IndexedI<K, V> d(K... key) {
        return DeleteKey(key);
    }
    
    /**
     * @see #deleteAllKeys(Collection...)
     */
    public default IndexedI<K, V> DeleteAllKeys(Collection<? extends K>... keys) {
        deleteAllKeys(keys);
        return this;
    }
    
    /**
     * @see #deleteAllKeys(Collection...)
     */
    public default IndexedI<K, V> D(Collection<? extends K>... keys) {
        return IndexedIS.this.DeleteAllKeys(keys);
    }
    
    /**
     * @see #deleteAllKeys(Collection...)
     */
    public default Map<K, V> deleteAllKeys(SequentialI<? extends K>... keys) {
        C.toStream(keys).forEach(c -> C.toStream(c.toCollection()).forEach(it -> deleteKey(it)));
        return toMap();
    }
    
    /**
     * @see #deleteAllKeys(Collection...)
     */
    public default IndexedI<K, V> DeleteAllKeys(SequentialI<? extends K>... keys) {
        deleteAllKeys(keys);
        return this;
    }
    
    /**
     * @see #deleteAllKeys(Collection...)
     */
    public default IndexedI<K, V> D(SequentialI<? extends K>... keys) {
        return DeleteAllKeys(keys);
    }
    
    /**
     * @see #deleteValue(Object...)
     */
    public default IndexedI<K, V> DeleteValue(V... value) {
        deleteValue(value);
        return this;
    }
    
    /**
     * @see #deleteAllValues(Collection...)
     */
    public default IndexedI<K, V> DeleteAllValues(Collection<? extends V>... values) {
        deleteAllValues(values);
        return this;
    }
    
    /**
     * @see #deleteAllValues(Collection...)
     */
    public default Map<K, V> deleteAllValues(SequentialI<? extends V>... values) {
        C.toStream(values).forEach(c -> C.toStream(c.toCollection()).forEach(it -> deleteValue(it)));
        return toMap();
    }
    
    /**
     * @see #deleteAllValues(Collection...)
     */
    public default IndexedI<K, V> DeleteAllValues(SequentialI<? extends V>... values) {
        deleteAllValues(values);
        return this;
    }
}

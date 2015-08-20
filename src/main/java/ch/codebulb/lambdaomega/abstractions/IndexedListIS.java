package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Adds syntactic sugar to a {@link IndexedListIAdd}.
 */
public interface IndexedListIS<K, V> extends IndexedListI<K, V> {
    /**
     * @see #insertAll(java.util.List)
     */
    public default Map<K, V> insertAll(Map<? extends K, ? extends V>... m) {
        return insertAll(C.toList(m));
    }
    
    /**
     * @see #insert(Object, Object)
     */
    public default IndexedListI<K, V> Insert(K index, V element) {
        insert(index, element);
        return this;
    }
    
    /**
     * @see #insert(Object, Object)
     */
    public default IndexedListI<K, V> i(K index, V element) {
        return Insert(index, element);
    }
    
    /**
     * @see #insertAll(java.util.List)
     */
    public default IndexedListI<K, V> InsertAll(Map<? extends K, ? extends V>... m) {
        insertAll(m);
        return this;
    }
    
    /**
     * @see #insertAll(java.util.List)
     */
    public default IndexedListI<K, V> I(Map<? extends K, ? extends V>... m) {
        return InsertAll(m);
    }
    
    /**
     * @see #insertAll(java.util.List)
     */
    public default Map<K, V> insertAll(IndexedI<? extends K, ? extends V>... m) {
        return insertAll(C.toStream(m).map(it -> it.toMap()).collect(Collectors.toList()));
    }
    
    /**
     * @see #insertAll(java.util.List)
     */
    public default IndexedListI<K, V> InsertAll(IndexedI<? extends K, ? extends V>... m) {
        insertAll(m);
        return this;
    }
    
    /**
     * @see #insertAll(java.util.List)
     */
    public default IndexedListI<K, V> I(IndexedI<? extends K, ? extends V>... m) {
        return InsertAll(m);
    }
    
    /**
     * @see #set(Object, Object)
     */
    public default IndexedListI<K, V> Set(K index, V element) {
        set(index, element);
        return this;
    }
    
    /**
     * @see #set(Object, Object)
     */
    public default IndexedListI<K, V> s(K index, V element) {
        return Set(index, element);
    }
    
    /**
     * @see #setAll(Map...)
     */
    public default IndexedListI<K, V> SetAll(Map<? extends K, ? extends V>... m) {
        setAll(m);
        return this;
    }
    
    /**
     * @see #setAll(Map...)
     */
    public default IndexedListI<K, V> S(Map<? extends K, ? extends V>... m) {
        return SetAll(m);
    }
    
    /**
     * @see #setAll(Map...)
     */
    public default Map<K, V> setAll(IndexedI<? extends K, ? extends V>... m) {
        return putAll(m);
    }
    
    /**
     * @see #setAll(Map...)
     */
    public default IndexedListI<K, V> SetAll(IndexedI<? extends K, ? extends V>... m) {
        setAll(m);
        return this;
    }
    
    /**
     * @see #setAll(Map...)
     */
    public default IndexedListI<K, V> S(IndexedI<? extends K, ? extends V>... m) {
        return SetAll(m);
    }
}

package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Adds syntactic sugar to a {@link IndexedListIAdd}.
 */
public interface IndexedListIS<K, V> extends IndexedListI<K, V> {
    public default Map<K, V> insertAll(Map<? extends K, ? extends V>... m) {
        return insertAll(C.toList(m));
    }
    
    public default IndexedListI<K, V> Insert(K index, V element) {
        insert(index, element);
        return this;
    }
    
    public default IndexedListI<K, V> i(K index, V element) {
        return Insert(index, element);
    }
    
    public default IndexedListI<K, V> InsertAll(Map<? extends K, ? extends V>... m) {
        insertAll(m);
        return this;
    }
    
    public default IndexedListI<K, V> I(Map<? extends K, ? extends V>... m) {
        return InsertAll(m);
    }
    
    public default Map<K, V> insertAll(IndexedI<? extends K, ? extends V>... m) {
        return insertAll(C.toStream(m).map(it -> it.toMap()).collect(Collectors.toList()));
    }
    
    public default IndexedListI<K, V> InsertAll(IndexedI<? extends K, ? extends V>... m) {
        insertAll(m);
        return this;
    }
    
    public default IndexedListI<K, V> I(IndexedI<? extends K, ? extends V>... m) {
        return InsertAll(m);
    }
    
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

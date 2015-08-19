package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import ch.codebulb.lambdaomega.M;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Adds a contract to work with "add" / "insert" functionality to a {@link IndexedListI}.
 */
public interface IndexedListIAdd<K, V> extends IndexedListI<K, V> {
    public default Map<K, V> insert(K index, V element) {
        if (containsAnyKey(index)) {
            throw new IndexAlreadyPresentException(index, toMap().get(index));
        }
        return put(index, element);
    }
    
    default Map<K, V> insertAll(List<Map<? extends K, ? extends V>> m) {
        M.E<K, V> duplicate = findDuplicateKeyInclThis(m);
        if (duplicate != null) {
            throw new IndexAlreadyPresentException(duplicate.k, duplicate.v);
        }
        return putAll(m);
    }
    
    public default Map<K, V> insertAll(Map<? extends K, ? extends V>... m) {
        return insertAll(C.toList(m));
    }
    
    public default IndexedListIAdd<K, V> Insert(K index, V element) {
        insert(index, element);
        return this;
    }
    
    public default IndexedListIAdd<K, V> i(K index, V element) {
        return Insert(index, element);
    }
    
    public default IndexedListIAdd<K, V> InsertAll(Map<? extends K, ? extends V>... m) {
        insertAll(m);
        return this;
    }
    
    public default IndexedListIAdd<K, V> I(Map<? extends K, ? extends V>... m) {
        return InsertAll(m);
    }
    
    public default Map<K, V> insertAll(IndexedI<? extends K, ? extends V>... m) {
        return insertAll(C.toStream(m).map(it -> it.toMap()).collect(Collectors.toList()));
    }
    
    public default IndexedListIAdd<K, V> InsertAll(IndexedI<? extends K, ? extends V>... m) {
        insertAll(m);
        return this;
    }
    
    public default IndexedListIAdd<K, V> I(IndexedI<? extends K, ? extends V>... m) {
        return InsertAll(m);
    }
    
    public static class IndexAlreadyPresentException extends RuntimeException {
        // cannot use generics in Throwable
        public final Object key;
        public final Object previousValue;

        public IndexAlreadyPresentException(Object key, Object previousValue) {
            super("Duplicate key found in map. Use #set(...) to override a value. Key: " + key + ", previous value: " + previousValue);
            this.key = key;
            this.previousValue = previousValue;
        }
    }
}

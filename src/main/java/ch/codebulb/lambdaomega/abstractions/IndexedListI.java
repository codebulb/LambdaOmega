package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import ch.codebulb.lambdaomega.M;
import static ch.codebulb.lambdaomega.M.e;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Adds a contract to work with sequential access to a {@link IndexedIS}. This interface makes no assumptions about the data structure's ordering.
 * It is loosely inspired by some methods of {@link List} which may actually also fit a {@link Map}.
 */
public interface IndexedListI<K, V> extends IndexedIS<K, V> {
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
    
    public default Map<K, V> set(K index, V element) {
        return put(index, element);
    }
    
    public default Map<K, V> setAll(Map<? extends K, ? extends V>... m) {
        return putAll(m);
    }

    public default K indexOf(V o) {
        return getKey(o);
    }
    
    default M.E<K, V> findDuplicateKeyInclThis(List<Map<? extends K, ? extends V>> maps) {
        List<Map<? extends K, ? extends V>> thisAndOtherMaps = Stream.concat(Stream.of(toMap()), C.toStream(maps)).collect(Collectors.toList());
        return findDuplicateKey(thisAndOtherMaps);
    }
    
    static <K, V> M.E<K, V> findDuplicateKey(List<Map<? extends K, ? extends V>> maps) {
        for (Map<? extends K, ? extends V> map : maps) {
            HashMap<K, V> copy = new HashMap<>(map);
            for (Map<? extends K, ? extends V> otherMap : maps) {
                if (map != otherMap) {
                    for (K key : otherMap.keySet()) {
                        V previousValue = copy.replace(key, null);
                        if (previousValue != null) {
                            return e(key, previousValue);
                        }
                    }
                }
            }
        }
        return null;
    }  
    
    /**
     * Signals that an attempt to {@link IndexedListI#insert(Object, Object)} an object with an already existing index / key
     * has been detected and rejected.
     */
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

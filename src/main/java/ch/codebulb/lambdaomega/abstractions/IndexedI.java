package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import ch.codebulb.lambdaomega.M;
import static ch.codebulb.lambdaomega.M.e;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An abstraction for a data structure with indexed (random) access. This interface makes no assumptions about the data structure's ordering.
 * It is loosely inspired by {@link Map}.
 */
public interface IndexedI<K, V> extends ReadonlyIndexedI<K, V> {
    public Map<K, V> toMap();
    
    public default Set<K> getKeys() {
        return toMap().keySet();
    }

    public default List<V> getValues() {
        return new ArrayList<>(toMap().values());
    }

    public default Set<M.E<K, V>> getEntries() {
        return toMap().entrySet().stream().map(it -> e(it)).collect(Collectors.toSet());
    }

    public default boolean containsKey(K... key) {
        return C.toStream(key).anyMatch(it -> toMap().containsKey(it));
    }
    
    public default boolean containsAnyKey(Collection<K>... key) {
        return C.toStream(key).anyMatch(c -> C.toStream(c).anyMatch(it -> toMap().containsKey(it)));
    }

    public default boolean containsValue(V... value) {
        return C.toStream(value).anyMatch(it -> toMap().containsValue(it));
    }
    
    public default boolean containsAnyValue(Collection<V>... value) {
        return C.toStream(value).anyMatch(c -> C.toStream(c).anyMatch(it -> toMap().containsValue(it)));
    }
    
    public default K getKey(V value) {
        Optional<M.E<K, V>> found = getEntries().stream().filter(it -> Objects.equals(it.v, value)).findFirst();
        if (found.isPresent()) {
            return found.get().k;
        }
        else {
            return null;
        }
    }
    
    public default Map<K, V> put(K key, V value) {
        toMap().put(key, value);
        return toMap();
    }
    
    default Map<K, V> putAll(List<Map<? extends K, ? extends V>> m) {
        C.toStream(m).forEach(it -> toMap().putAll(it));
        return toMap();
    }
    
    public default Map<K, V> putAll(Map<? extends K, ? extends V>... m) {
        return putAll(C.toList(m));
    }
    
    public default V putIfAbsent(K key, V value) {
        return toMap().putIfAbsent(key, value);
    }
    
    public default V replace(K key, V value) {
        return toMap().replace(key, value);
    }

    public default boolean replace(K key, V oldValue, V newValue) {
        return toMap().replace(key, oldValue, newValue);
    }
    
    public default Map<K, V> deleteKey(K... key) {
        C.toStream(key).forEach(it -> toMap().remove(it));
        return toMap();
    }
    
    public default Map<K, V> deleteAllKeys(Collection<? extends K>... keys) {
        C.toStream(keys).forEach(c -> C.toStream(c).forEach(it -> deleteKey(it)));
        return toMap();
    }
    
    public default Map<K, V> deleteValue(V... value) {
        C.toStream(value).forEach(it -> {
            K found = getKey(it);
            if (found != null) {
                toMap().remove(found);
            }
        });
        return toMap();
    }
    
    public default Map<K, V> deleteAllValues(Collection<? extends V>... values) {
        List<K> found = getEntries().stream()
                .filter(e -> C.toStream(values).anyMatch(c -> c.stream().anyMatch(it -> Objects.equals(e.v, it))))
                .map(it -> it.k)
                .collect(Collectors.toList());
        found.stream().forEach(it -> toMap().remove(it));
        return toMap();
    }

    public default boolean deleteIfMatches(K key, V value) {
        return toMap().remove(key, value);
    }
    
    @Override
    public default <VN extends V> VN get(K key) {
        return (VN) toMap().get(key);
    }

    @Override
    public default <VN extends V> VN getOrDefault(K key, V defaultValue) {
        return (VN) toMap().getOrDefault(key, defaultValue);
    }
}

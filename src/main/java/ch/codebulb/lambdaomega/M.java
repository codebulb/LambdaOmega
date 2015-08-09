package ch.codebulb.lambdaomega;

import ch.codebulb.lambdaomega.M.E;
import ch.codebulb.lambdaomega.abstractions.IndexedI;
import ch.codebulb.lambdaomega.abstractions.SequentialIS;
import ch.codebulb.lambdaomega.abstractions.IndexedListIS;
import ch.codebulb.lambdaomega.abstractions.SequentialI;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The "M" stands for "map". An implementation of a wrapper API for {@link Map} which provides both indexed access (from <code>K</code> to <code>V</code>)
 * and sequential access (of type {@link E} with generic parameters <code>K</code> to <code>V</code>).
 * 
 * @param <K> the key type
 * @param <V> the value type
 */
public class M<K, V> extends C<M.E<K, V>, K, V> implements SequentialIS<M.E<K, V>>, IndexedListIS<K, V> {
    public final Map<K, V> m;

    private M(Map<K, V> map) {
        this.m = map;
    }
    
    public static M m() {
        return m(new LinkedHashMap<>());
    }
    
    public static <K, V> M<K, V> m(Map<K, V> map) {
        return new M<>(map);
    }
    
    public static <K, V> M<K, V> m(Class<K> keyClass, Class<V> valueClas) {
        return M.<K, V> m();
    }
    
    public static <V> M<String, V> m(Class<V> valueClas) {
        return M.<String, V> m();
    }
    
    public static <K, V> M<K, V> m(E<K, V>... entries) {
        M<K, V> map = m();
        Arrays.stream(entries).forEach(it -> map.i(it.k, it.v));
        return map;
    }
    
    public static <K, V> M<K, V> m(Function<K, V> defaultValue) {
        return (M<K, V>) M.<K, V>m().WithDefault(defaultValue);
    }
    
    public static <K, V> Map<K, V> map(Class<K> keyClass, Class<V> valueClas) {
        return new LinkedHashMap<>();
    }
    
    public static <K, V> Map<K, V> map(E<K, V>... entries) {
        return m(entries).m;
    }
    
    public static <K, V, K2 extends K, V2 extends V> M<K, V> m(K2 k, V2 v) {
        return (M<K, V>) M.<K, V>m().i(k, v);
    }
    
    @Override
    public Collection<E<K, V>> toCollection() {
        return m.entrySet().stream().map(it -> e(it)).collect(Collectors.toSet());
    }

    @Override
    public Map<K, V> toMap() {
        return m;
    }
    
    @Override
    public <VN extends V> VN get(K key) {
        if (containsKey(key)) {
            return (VN) m.get(key);
        }
        else {
            V ret = defaultValue.apply(key);
            put(key, ret);
            return (VN) ret;
        }
    }

    @Override
    public List<E<K, V>> add(E<K, V>... e) {
        C.toStream(e).forEach(it -> put(it.k, it.v));
        return toList();
    }

    @Override
    public List<E<K, V>> clear() {
        m.clear();
        return toList();
    }

    @Override
    public List<E<K, V>> remove(E<K, V>... value) {
        return removeAll(C.toList(value));
    }

    @Override
    public List<E<K, V>> removeAll(Collection<? extends E<K, V>>... c) {
        C.toStream(c).forEach(col -> {
            Map<K, V> asMap = C.toStream(col).map(it -> e(it.k, it.v)).collect(Collectors.toMap(it -> it.k, it -> it.v));
            asMap.forEach((k, v) -> m.remove(k, v));
        });
        return toList();
    }

    @Override
    public List<E<K, V>> retainAll(Collection<? extends E<K, V>>... c) {
        C.toStream(c).forEach(col -> {
            Map<K, V> asMap = C.toStream(col).map(it -> e(it.k, it.v)).collect(Collectors.toMap(it -> it.k, it -> it.v));
            List<K> found = L.list();
            asMap.forEach((k, v) -> {
                if (!Objects.equals(m.get(k), v)) {
                    found.add(k);
                }
            });
            found.stream().forEach(k -> m.remove(k));
        });
        return toList();
    }
    
    

    @Override
    public List<E<K, V>> addAll(Collection<? extends E<K, V>>... c) {
        C.toStream(c).forEach(col -> {
            Map<K, V> asMap = C.toStream(col).map(it -> e(it.k, it.v)).collect(Collectors.toMap(it -> it.k, it -> it.v));
            putAll(asMap);
        });
        return toList();
    }
    
    @Override
    public List<E<K, V>> addAll(SequentialI<? extends E<K, V>>... c) {
        C.toStream(c).forEach(col -> {
            Map<K, V> asMap = C.toStream(col.toCollection()).map(it -> e(it.k, it.v)).collect(Collectors.toMap(it -> it.k, it -> it.v));
            putAll(asMap);
        });
        return toList();
    }

    @Override
    public M<K, V> A(SequentialI<? extends E<K, V>>... c) {
        return (M<K, V>) SequentialIS.super.A(c);
    }

    @Override
    public M<K, V> AddAll(SequentialI<? extends E<K, V>>... c) {
        return (M<K, V>) SequentialIS.super.AddAll(c);
    }

    @Override
    public M<K, V> A(Collection<? extends E<K, V>>... c) {
        return (M<K, V>) SequentialIS.super.A(c);
    }

    @Override
    public M<K, V> AddAll(Collection<? extends E<K, V>>... c) {
        return (M<K, V>) SequentialIS.super.AddAll(c);
    }

    @Override
    public M<K, V> a(E<K, V>... e) {
        return (M<K, V>) SequentialIS.super.a(e);
    }

    @Override
    public M<K, V> Add(E<K, V>... e) {
        return (M<K, V>) SequentialIS.super.Add(e);
    }

    @Override
    public M<K, V> I(IndexedI<? extends K, ? extends V>... m) {
        return (M<K, V>) IndexedListIS.super.I(m);
    }

    @Override
    public M<K, V> InsertAll(IndexedI<? extends K, ? extends V>... m) {
        return (M<K, V>) IndexedListIS.super.InsertAll(m);
    }

    @Override
    public M<K, V> I(Map<? extends K, ? extends V>... m) {
        return (M<K, V>) IndexedListIS.super.I(m);
    }

    @Override
    public M<K, V> InsertAll(Map<? extends K, ? extends V>... m) {
        return (M<K, V>) IndexedListIS.super.InsertAll(m);
    }

    @Override
    public M<K, V> i(K index, V element) {
        return (M<K, V>) IndexedListIS.super.i(index, element);
    }

    @Override
    public M<K, V> Insert(K index, V element) {
        return (M<K, V>) IndexedListIS.super.Insert(index, element);
    }

    @Override
    public M<K, V> S(IndexedI<? extends K, ? extends V>... m) {
        return (M<K, V>) IndexedListIS.super.S(m);
    }

    @Override
    public M<K, V> SetAll(IndexedI<? extends K, ? extends V>... m) {
        return (M<K, V>) IndexedListIS.super.SetAll(m);
    }

    @Override
    public M<K, V> S(Map<? extends K, ? extends V>... m) {
        return (M<K, V>) IndexedListIS.super.S(m);
    }

    @Override
    public M<K, V> SetAll(Map<? extends K, ? extends V>... m) {
        return (M<K, V>) IndexedListIS.super.SetAll(m);
    }

    @Override
    public M<K, V> s(K index, V element) {
        return (M<K, V>) IndexedListIS.super.s(index, element);
    }

    @Override
    public M<K, V> Set(K index, V element) {
        return (M<K, V>) IndexedListIS.super.Set(index, element);
    }

    @Override
    public M<K, V> D(SequentialI<? extends K>... keys) {
        return (M<K, V>) IndexedListIS.super.D(keys);
    }

    @Override
    public M<K, V> DeleteAllKeys(SequentialI<? extends K>... keys) {
        return (M<K, V>) IndexedListIS.super.DeleteAllKeys(keys);
    }

    @Override
    public M<K, V> D(Collection<? extends K>... keys) {
        return (M<K, V>) IndexedListIS.super.D(keys);
    }

    @Override
    public M<K, V> DeleteAllKeys(Collection<? extends K>... keys) {
        return (M<K, V>) IndexedListIS.super.DeleteAllKeys(keys);
    }

    @Override
    public M<K, V> d(K... key) {
        return (M<K, V>) IndexedListIS.super.d(key);
    }

    @Override
    public M<K, V> DeleteKey(K... key) {
        return (M<K, V>) IndexedListIS.super.DeleteKey(key);
    }

    @Override
    public M<K, V> P(IndexedI<? extends K, ? extends V>... m) {
        return (M<K, V>) IndexedListIS.super.P(m);
    }

    @Override
    public M<K, V> PutAll(IndexedI<? extends K, ? extends V>... m) {
        return (M<K, V>) IndexedListIS.super.PutAll(m);
    }

    @Override
    public M<K, V> P(Map<? extends K, ? extends V>... m) {
        return (M<K, V>) IndexedListIS.super.P(m);
    }

    @Override
    public M<K, V> PutAll(Map<? extends K, ? extends V>... m) {
        return (M<K, V>) IndexedListIS.super.PutAll(m);
    }

    @Override
    public M<K, V> p(K key, V value) {
        return (M<K, V>) IndexedListIS.super.p(key, value);
    }

    @Override
    public M<K, V> Put(K key, V value) {
        return (M<K, V>) IndexedListIS.super.Put(key, value);
    }

    @Override
    public L<E<K, V>> R(SequentialI<? extends E<K, V>>... c) {
        return (L<E<K, V>>) SequentialIS.super.R(c);
    }

    @Override
    public L<E<K, V>> RemoveAll(SequentialI<? extends E<K, V>>... c) {
        return (L<E<K, V>>) SequentialIS.super.RemoveAll(c);
    }

    @Override
    public L<E<K, V>> R(Collection<? extends E<K, V>>... c) {
        return (L<E<K, V>>) SequentialIS.super.R(c);
    }

    @Override
    public L<E<K, V>> RemoveAll(Collection<? extends E<K, V>>... c) {
        return (L<E<K, V>>) SequentialIS.super.RemoveAll(c);
    }

    @Override
    public L<E<K, V>> r(E<K, V>... value) {
        return (L<E<K, V>>) SequentialIS.super.r(value);
    }

    @Override
    public L<E<K, V>> Remove(E<K, V>... value) {
        return (L<E<K, V>>) SequentialIS.super.Remove(value);
    }

    @Override
    public M<K, V> DeleteAllValues(SequentialI<? extends V>... values) {
        return (M<K, V>) IndexedListIS.super.DeleteAllValues(values);
    }

    @Override
    public M<K, V> DeleteAllValues(Collection<? extends V>... values) {
        return (M<K, V>) IndexedListIS.super.DeleteAllValues(values);
    }

    @Override
    public M<K, V> DeleteValue(V... value) {
        return (M<K, V>) IndexedListIS.super.DeleteValue(value);
    }
    
    @Override
    public M<K, V> WithDefault(Function<K, V> defaultValue) {
        return (M<K, V>) super.WithDefault(defaultValue);
    }

    @Override
    public M<K, V> Seq() {
        return (M<K, V>) super.Seq();
    }

    @Override
    public M<K, V> Sequential() {
        return (M<K, V>) super.Sequential();
    }

    @Override
    public M<K, V> Par() {
        return (M<K, V>) super.Par();
    }

    @Override
    public M<K, V> Parallel() {
        return (M<K, V>) super.Parallel();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.m);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final M<?, ?> other = (M<?, ?>) obj;
        if (!Objects.equals(this.m, other.m)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "M" + m.toString();
    }
    
    public static class MapEntryKeyAlreadyPresentException extends RuntimeException {
        // cannot use generics in Throwable
        public final Object key;
        public final Object previousValue;

        public MapEntryKeyAlreadyPresentException(Object key, Object previousValue) {
            super("Duplicate key found in map. Use #set(...) to override a value. Key: " + key + ", previous value: " + previousValue);
            this.key = key;
            this.previousValue = previousValue;
        }
    }
    
    public static <K, V> E<K, V> e(K k, V v) {
        return new E(k, v);
    }
    
    public static <K, V> E<K, V> e(Map.Entry<K, V> entry) {
        return new E(entry.getKey(), entry.getValue());
    }

    public static class E<K, V> {
        public final K k;
        public final V v;

        public E(K k, V v) {
            this.k = k;
            this.v = v;
        }
        
        public Map.Entry<K, V> toEntry() {
            return new AbstractMap.SimpleEntry<>(k, v);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + Objects.hashCode(this.k);
            hash = 59 * hash + Objects.hashCode(this.v);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final E<?, ?> other = (E<?, ?>) obj;
            if (!Objects.equals(this.k, other.k)) {
                return false;
            }
            if (!Objects.equals(this.v, other.v)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "e(" + k + ", " + v + ')';
        }
    }
}
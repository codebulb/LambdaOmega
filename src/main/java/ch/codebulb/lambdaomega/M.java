package ch.codebulb.lambdaomega;

import ch.codebulb.lambdaomega.M.E;
import ch.codebulb.lambdaomega.abstractions.IndexedI;
import ch.codebulb.lambdaomega.abstractions.SequentialIS;
import ch.codebulb.lambdaomega.abstractions.IndexedListIS;
import ch.codebulb.lambdaomega.abstractions.SequentialI;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
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
        C.toStream(entries).forEach(it -> map.i(it.k, it.v));
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
        if (containsAnyKey(key)) {
            return (VN) m.get(key);
        }
        else {
            V ret = defaultValue.apply(key);
            put(key, ret);
            return (VN) ret;
        }
    }

    @Override
    public Set<E<K, V>> add(E<K, V>... e) {
        C.toStream(e).forEach(it -> put(it.k, it.v));
        return toSet();
    }

    @Override
    public Set<E<K, V>> clear() {
        m.clear();
        return toSet();
    }

    @Override
    public Set<E<K, V>> remove(E<K, V>... value) {
        return removeAll(C.toList(value));
    }

    @Override
    public Set<E<K, V>> removeAll(Collection<? extends E<K, V>>... c) {
        C.toStream(c).forEach(col -> {
            Map<K, V> asMap = C.toStream(col).map(it -> e(it.k, it.v)).collect(Collectors.toMap(it -> it.k, it -> it.v));
            asMap.forEach((k, v) -> m.remove(k, v));
        });
        return toSet();
    }

    @Override
    public Set<E<K, V>> retainAll(Collection<? extends E<K, V>>... c) {
        Map<K, V> allMap = new HashMap<>();
        C.toStream(c).forEach(col -> {
            Map<K, V> asMap = C.toStream(col).map(it -> e(it.k, it.v)).collect(Collectors.toMap(it -> it.k, it -> it.v));
            allMap.putAll(asMap);
        });
        
        List<K> found = L.list();
        m.forEach((k, v) -> {
            if (!Objects.equals(allMap.get(k), v)) {
                found.add(k);
            }
        });
        found.stream().forEach(k -> m.remove(k));
        
        return toSet();
    }

    @Override
    public Set<E<K, V>> addAll(Collection<? extends E<K, V>>... c) {
        C.toStream(c).forEach(col -> {
            Map<K, V> asMap = C.toStream(col).map(it -> e(it.k, it.v)).collect(Collectors.toMap(it -> it.k, it -> it.v));
            putAll(asMap);
        });
        return toSet();
    }
    
    @Override
    public Set<E<K, V>> addAll(SequentialI<? extends E<K, V>>... c) {
        C.toStream(c).forEach(col -> {
            Map<K, V> asMap = C.toStream(col.toCollection()).map(it -> e(it.k, it.v)).collect(Collectors.toMap(it -> it.k, it -> it.v));
            putAll(asMap);
        });
        return toSet();
    }
    
    @Override
    // Use Set as the return type for operation on entries because entries are kept in a Set.
    public <R> Collector<R, ?, Set<R>> createCollector() {
        return Collectors.toSet();
    }

    @Override
    @Deprecated
    public String join(CharSequence delimiter) {
        throw new UnsupportedOperationException("Because the order of entries of a map is not defined, "
                + "joining them leads to undefined results; therefore, it is forbidden.");
    }

    @Override
    public <R> Set<R> map(Function<E<K, V>, R> function) {
        return (Set<R>)SequentialIS.super.map(function);
    }

    @Override
    @Deprecated
    public <N> List<N> flatten() {
        throw new UnsupportedOperationException("Flatten is not supported by map.");
    }

    @Override
    @Deprecated
    public <N> List<N> flattenDeep() {
        throw new UnsupportedOperationException("Flatten is not supported by map.");
    }

    @Override
    @Deprecated
    public <N> L<N> FlattenDeep() {
        return SequentialIS.super.FlattenDeep();
    }

    @Override
    @Deprecated
    public <N> L<N> Flatten() {
        return SequentialIS.super.Flatten();
    }

    @Override
    public <R> Map<R, Set<E<K, V>>> groupBy(Function<? super E<K, V>, ? extends R> classifier) {
        return (Map<R, Set<E<K, V>>>)SequentialIS.super.groupBy(classifier);
    }

    @Override
    public Map<Boolean, Set<E<K, V>>> partition(Predicate<? super E<K, V>> predicate) {
        return (Map<Boolean, Set<E<K, V>>>)SequentialIS.super.partition(predicate);
    }

    @Override
    public Set<E<K, V>> findAll(Predicate<E<K, V>> predicate) {
        return (Set<E<K, V>>) SequentialIS.super.findAll(predicate);
    }

    @Override
    public Set<E<K, V>> filter(Predicate<E<K, V>> predicate) {
        return (Set<E<K, V>>) SequentialIS.super.filter(predicate);
    }
    
    @Override
    public Set<E<K, V>> reject(Predicate<E<K, V>> predicate) {
        return (Set<E<K, V>>) SequentialIS.super.reject(predicate);
    }

    @Override
    public <R> Set<R> map(BiFunction<K, V, R> function) {
        return (Set<R>)IndexedListIS.super.map(function);
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
    public Set<E<K, V>> removeAll(SequentialI<? extends E<K, V>>... c) {
        return (Set<E<K, V>>) SequentialIS.super.removeAll(c);
    }

    @Override
    public M<K, V> R(SequentialI<? extends E<K, V>>... c) {
        return (M<K, V>) SequentialIS.super.R(c);
    }

    @Override
    public M<K, V> RemoveAll(SequentialI<? extends E<K, V>>... c) {
        return (M<K, V>) SequentialIS.super.RemoveAll(c);
    }

    @Override
    public M<K, V> R(Collection<? extends E<K, V>>... c) {
        return (M<K, V>) SequentialIS.super.R(c);
    }

    @Override
    public M<K, V> RemoveAll(Collection<? extends E<K, V>>... c) {
        return (M<K, V>) SequentialIS.super.RemoveAll(c);
    }

    @Override
    public M<K, V> r(E<K, V>... value) {
        return (M<K, V>) SequentialIS.super.r(value);
    }

    @Override
    public M<K, V> Remove(E<K, V>... value) {
        return (M<K, V>) SequentialIS.super.Remove(value);
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
    
    public static <K, V> E<K, V> e(K k, V v) {
        return new E(k, v);
    }
    
    public static <K, V> E<K, V> e(Map.Entry<K, V> entry) {
        return new E(entry.getKey(), entry.getValue());
    }

    /**
     * The "E" stands for "entry". Represents a single {@link Map} entry and can be converted to a {@link Entry}.
     *
     * @param <K> the key type
     * @param <V> the value type
     */
    public static class E<K, V> implements Comparable<E<K, V>> {
        public final K k;
        public final V v;

        public E(K k, V v) {
            this.k = k;
            this.v = v;
        }
        
        public Map.Entry<K, V> toEntry() {
            return new AbstractMap.SimpleEntry<>(k, v);
        }

        public K getK() {
            return k;
        }

        public V getV() {
            return v;
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

        @Override
        public int compareTo(E<K, V> obj) {
            if (obj == null) {
                return 1;
            }
            
            if (!(k instanceof Comparable)) {
                throw new ClassCastException(k.getClass().getName() + " cannot be cast to java.lang.Comparable");
            }
            int ret = ((Comparable<K>)k).compareTo(obj.k);
            if (ret != 0) {
                return ret;
            }
            
            if (!(v instanceof Comparable)) {
                throw new ClassCastException(v.getClass().getName() + " cannot be cast to java.lang.Comparable");
            }
            ret = ((Comparable<V>)v).compareTo(obj.v);
            
            return ret;
        }
    }
}

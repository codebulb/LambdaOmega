package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.F.compareAsc;
import ch.codebulb.lambdaomega.M.E;
import ch.codebulb.lambdaomega.abstractions.IndexedI;
import ch.codebulb.lambdaomega.abstractions.IndexedListIS;
import ch.codebulb.lambdaomega.abstractions.OrderedSequentialS;
import ch.codebulb.lambdaomega.abstractions.SequentialI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The "L" stands for "list". An implementation of a wrapper API for {@link List} which provides both sequential access (of type <code>T</code>)
 * and indexed access (from {@link Integer} to <code>T</code>).
 *
 * @param <T> the entry type
 */
public class L<T> extends C<T, Integer, T> implements OrderedSequentialS<T>, IndexedListIS<Integer, T> {
    static boolean TEST_DISABLE_HELPER_MAP_CONVERSION;
    public final List<T> l;
    
    L(List<T> list) {
        this.l = new ArrayList<>(list);
    }
    
    public static <T> L<T> l() {
        return new L<>(new ArrayList<>());
    }
    
    public static <T> L<T> l(T... ts) {
        return L(C.toStream(ts));
    }
    
    public static <T> L<L<T>> l(L<T>... lists) {
        return L(Arrays.asList(lists).stream());
    }
    
    public static <T> L<T> L(Stream<T> stream) {
        return new L(stream.collect(Collectors.toList()));
    }
    
    public static <T> L<T> L(Collection<T> collection) {
        return L(C.toStream(collection));
    }
    
    public static <T> L<List<T>> L(L<T>... lists) {
        return L.L(Arrays.asList(lists).stream().map(it -> it.l).collect(Collectors.toList()));
    }
    
    public static <T> L<T> l(Class<T> clazz) {
        return l();
    }
    
    public static <T> List<T> list(T... ts) {
        return l(ts).l;
    }
    
    public static <T> L<T> l(int times, Function<Integer, T> generator) {
        return L(R.r(0).to(times).r.boxed().map(generator));
    }
        
    public static <T> List<T> list(int times, Supplier<T> generator) {
        return L(Stream.generate(generator).limit(times)).l;
    }

    @Override
    public Collection<T> toCollection() {
        return l;
    }

    @Override
    public List<T> toList() {
        return l;
    }
    
    @Override
    public Map<Integer, T> toMap() {
        if (TEST_DISABLE_HELPER_MAP_CONVERSION) {
            /*
             * Some interface method have default implementations which internally use #toMap().
             * This is senisble for a Map-based implementation, but for a List-based implementation,
             * first creating a helper Map would likely be overhead.
             *
             * Find a more efficient solution in these individual cases.
             *
             * For the tests, we use this debug flag to alert ourselves that there are still unoptimized operations.
             */
            throw new UnsupportedOperationException("Methods which use #toMap() must be overridden for performance reasons.");
        }
        else {
            throw new UnsupportedOperationException("#toMap() is not supported for L.");
        }
    }
    
    Map<Integer, T> toInternalMap() {
        if (!isParallel()) {
            // use fast sequential counting
            return mapEntries(new Function<T, E<Integer, T>>() {
                int i = 0;
                @Override
                public E<Integer, T> apply(T t) {
                    return M.e(i++, t);
                }
            });
        }
        else {
            // retrieve each index individually
            return mapEntries(new Function<T, E<Integer, T>>() {
                @Override
                public E<Integer, T> apply(T t) {
                    return M.e(indexOf(t), t);
                }
            });
        }
    }
    
    @Override
    public T get(Integer index) {
        if (containsKey(index)) {
            return l.get(index);
        }
        else {
            return defaultValue.apply(index);
        }
    }
    
    @Override
    public <VN extends T> VN getOrDefault(Integer key, T defaultValue) {
        if (containsKey(key)) {
            return (VN) l.get(key);
        }
        else {
            return (VN) defaultValue;
        }
    }
    
    @Override
    public List<T> getValues() {
        return l;
    }
    
    @Override
    public Set<Integer> getKeys() {
        return new HashSet<>(R.r(0).to(size()).list);
    }
    
    @Override
    public boolean containsKey(Integer... keys) {
        return C.toStream(keys).anyMatch(it -> it >= 0 && size() > it);
    }
    
    @Override
    public boolean containsValue(T... values) {
        return C.toStream(values).anyMatch(it -> l.contains(it));
    }

    @Override
    public Integer indexOf(T o) {
        return l.indexOf(o);
    }

    @Override
    public Map<Integer, T> put(Integer key, T value) {
        l.set(key, value);
        return toInternalMap();
    }

    @Override
    public Map<Integer, T> putAll(List<Map<? extends Integer, ? extends T>> m) {
        C.toStream(m).forEach(it -> it.forEach((i, e) -> put(i, e)));
        return toInternalMap();
    }

    @Override
    public Map<Integer, T> putAll(Map<? extends Integer, ? extends T>... m) {
        return putAll(C.toList(m));
    }
    
    @Override
    public T putIfAbsent(Integer key, T value) {
        T currentValue = get(key);
        if (containsKey(key) && currentValue == null) {
            set(key, value);
            return currentValue;
        }
        else {
            return currentValue;
        }
    }

    @Override
    public Map<Integer, T> insert(Integer index, T element) {
        if (containsKey(index)) {
            throw new M.MapEntryKeyAlreadyPresentException(index, l.get(index));
        }
        add(element);
        return toInternalMap();
    }

    @Override
    public Map<Integer, T> insertAll(List<Map<? extends Integer, ? extends T>> m) {
        M.m(toInternalMap()).insertAll(m);
        C.toStream(m).forEach(col -> {
            col.entrySet().stream().sorted(compareAsc(it -> it.getKey())).forEach(it -> add(it.getValue()));
        });
        return toInternalMap();
    }
    
    

    @Override
    public T replace(Integer key, T value) {
        if (!containsKey(key)) {
            return null;
        }
        T currentValue = get(key);
        set(key, value);
        return currentValue;
    }

    @Override
    public boolean replace(Integer key, T oldValue, T newValue) {
        if (containsKey(key) && l.get(key).equals(oldValue)) {
            set(key, newValue);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean deleteIfMatches(Integer key, T value) {
        if (l.get(key) == value) {
            deleteKey(key);
            return true;
        }
        else {
            return false;
        }
    }
    
    @Override
    public Map<Integer, T> deleteKey(Integer... key) {
        l(key).SortAscBy().forEach((it, i) -> l.remove(it-i));
        return toInternalMap();
    }

    @Override
    public Map<Integer, T> deleteAllKeys(Collection<? extends Integer>... keys) {
        l(keys).<Integer> Flatten().SortAscBy().forEach((it, i) -> l.remove(it-i));
        return toInternalMap();
    }
    
    @Override
    public Map<Integer, T> deleteValue(T... value) {
        remove(value);
        return toInternalMap();
    }
    @Override
    public Map<Integer, T> deleteAllValues(Collection<? extends T>... values) {
        removeAll(values);
        return toInternalMap();
    }

    @Override
    public void forEach(BiConsumer<Integer, T> action) {
        if (!isParallel()) {
            // use fast sequential counting
            stream().forEach(new Consumer<T>() {
                int i = 0;
                @Override
                public void accept(T t) {
                    action.accept(i++, t);
                }
            });
        }
        else {
            // retrieve each index individually
            stream().forEach(new Consumer<T>() {
                List<T> list = stream().collect(Collectors.toList());
                @Override
                public void accept(T t) {
                    action.accept(list.indexOf(t), t);
                }
            });
        }
    }

    @Override
    public L<T> S(IndexedI<? extends Integer, ? extends T>... m) {
        return (L<T>) IndexedListIS.super.S(m);
    }

    @Override
    public L<T> SetAll(IndexedI<? extends Integer, ? extends T>... m) {
        return (L<T>) IndexedListIS.super.SetAll(m);
    }

    @Override
    public L<T> S(Map<? extends Integer, ? extends T>... m) {
        return (L<T>) IndexedListIS.super.S(m);
    }

    @Override
    public L<T> SetAll(Map<? extends Integer, ? extends T>... m) {
        return (L<T>) IndexedListIS.super.SetAll(m);
    }

    @Override
    public L<T> Set(Integer index, T element) {
        return (L<T>) IndexedListIS.super.Set(index, element);
    }

    @Override
    public L<T> D(SequentialI<? extends Integer>... keys) {
        return (L<T>) IndexedListIS.super.D(keys);
    }

    @Override
    public L<T> DeleteAllKeys(SequentialI<? extends Integer>... keys) {
        return (L<T>) IndexedListIS.super.DeleteAllKeys(keys);
    }

    @Override
    public L<T> D(Collection<? extends Integer>... keys) {
        return (L<T>) IndexedListIS.super.D(keys);
    }

    @Override
    public L<T> DeleteAllKeys(Collection<? extends Integer>... keys) {
        return (L<T>) IndexedListIS.super.DeleteAllKeys(keys);
    }

    @Override
    public L<T> DeleteKey(Integer... key) {
        return (L<T>) IndexedListIS.super.DeleteKey(key);
    }

    @Override
    public M<Integer, T> DeleteAllValues(SequentialI<? extends T>... values) {
        return (M<Integer, T>) IndexedListIS.super.DeleteAllValues(values);
    }

    @Override
    public M<Integer, T> DeleteAllValues(Collection<? extends T>... values) {
        return (M<Integer, T>) IndexedListIS.super.DeleteAllValues(values);
    }

    @Override
    public M<Integer, T> DeleteValue(T... value) {
        return (M<Integer, T>) IndexedListIS.super.DeleteValue(value);
    }

    @Override
    public L<T> P(IndexedI<? extends Integer, ? extends T>... m) {
        return (L<T>) IndexedListIS.super.P(m);
    }

    @Override
    public L<T> PutAll(IndexedI<? extends Integer, ? extends T>... m) {
        return (L<T>) IndexedListIS.super.PutAll(m);
    }

    @Override
    public L<T> P(Map<? extends Integer, ? extends T>... m) {
        return (L<T>) IndexedListIS.super.P(m);
    }

    @Override
    public L<T> PutAll(Map<? extends Integer, ? extends T>... m) {
        return (L<T>) IndexedListIS.super.PutAll(m);
    }

    @Override
    public L<T> Put(Integer key, T value) {
        return (L<T>) IndexedListIS.super.Put(key, value);
    }

    @Override
    public L<T> Get(Integer... keys) {
        return (L<T>) IndexedListIS.super.Get(keys);
    }

    @Override
    public L<T> R(SequentialI<? extends T>... c) {
        return (L<T>) OrderedSequentialS.super.R(c);
    }

    @Override
    public L<T> RemoveAll(SequentialI<? extends T>... c) {
        return (L<T>) OrderedSequentialS.super.RemoveAll(c);
    }

    @Override
    public L<T> R(Collection<? extends T>... c) {
        return (L<T>) OrderedSequentialS.super.R(c);
    }

    @Override
    public L<T> RemoveAll(Collection<? extends T>... c) {
        return (L<T>) OrderedSequentialS.super.RemoveAll(c);
    }

    @Override
    public L<T> Remove(T... value) {
        return (L<T>) OrderedSequentialS.super.Remove(value);
    }

    @Override
    public L<T> A(SequentialI<? extends T>... c) {
        return (L<T>) OrderedSequentialS.super.A(c);
    }

    @Override
    public L<T> AddAll(SequentialI<? extends T>... c) {
        return (L<T>) OrderedSequentialS.super.AddAll(c);
    }

    @Override
    public L<T> A(Collection<? extends T>... c) {
        return (L<T>) OrderedSequentialS.super.A(c);
    }

    @Override
    public L<T> AddAll(Collection<? extends T>... c) {
        return (L<T>) OrderedSequentialS.super.AddAll(c);
    }

    @Override
    public L<T> Add(T... e) {
        return (L<T>) OrderedSequentialS.super.Add(e);
    }

    @Override
    public L<T> I(IndexedI<? extends Integer, ? extends T>... m) {
        return (L<T>) IndexedListIS.super.I(m);
    }

    @Override
    public L<T> InsertAll(IndexedI<? extends Integer, ? extends T>... m) {
        return (L<T>) IndexedListIS.super.InsertAll(m);
    }

    @Override
    public L<T> I(Map<? extends Integer, ? extends T>... m) {
        return (L<T>) IndexedListIS.super.I(m);
    }

    @Override
    public L<T> InsertAll(Map<? extends Integer, ? extends T>... m) {
        return (L<T>) IndexedListIS.super.InsertAll(m);
    }

    @Override
    public L<T> i(Integer index, T element) {
        return (L<T>) IndexedListIS.super.i(index, element);
    }

    @Override
    public L<T> Insert(Integer index, T element) {
        return (L<T>) IndexedListIS.super.Insert(index, element);
    }
    
    @Override
    public L<T> s(Integer index, T element) {
        return (L<T>) IndexedListIS.super.s(index, element);
    }

    @Override
    public L<T> d(Integer... key) {
        return (L<T>) IndexedListIS.super.d(key);
    }

    @Override
    public L<T> p(Integer key, T value) {
        return (L<T>) IndexedListIS.super.p(key, value);
    }

    @Override
    public L<T> r(T... value) {
        return (L<T>) OrderedSequentialS.super.r(value);
    }

    @Override
    public L<T> a(T... e) {
        return (L<T>) OrderedSequentialS.super.a(e);
    }

    @Override
    public L<T> WithDefault(Function<Integer, T> defaultValue) {
        return (L<T>) super.WithDefault(defaultValue);
    }

    @Override
    public L<T> Seq() {
        return (L<T>) super.Seq();
    }

    @Override
    public L<T> Sequential() {
        return (L<T>) super.Sequential();
    }

    @Override
    public L<T> Par() {
        return (L<T>) super.Par();
    }

    @Override
    public L<T> Parallel() {
        return (L<T>) super.Parallel();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.l);
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
        final L<?> other = (L<?>) obj;
        if (!Objects.equals(this.l, other.l)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "L" + l.toString();
    }
}

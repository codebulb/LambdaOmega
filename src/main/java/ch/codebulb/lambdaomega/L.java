package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.F.compareAsc;
import ch.codebulb.lambdaomega.M.E;
import static ch.codebulb.lambdaomega.M.e;
import ch.codebulb.lambdaomega.abstractions.IndexedI;
import ch.codebulb.lambdaomega.abstractions.IndexedListIS;
import ch.codebulb.lambdaomega.abstractions.OrderedSequentialS;
import ch.codebulb.lambdaomega.abstractions.SequentialI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The "L" stands for "list". An implementation of a wrapper API for {@link List} which provides both sequential access (of type <code>T</code>)
 * and indexed access (from {@link Integer} to <code>T</code>).<p/>
 * 
 * The constructor of this class is not visible; use one of the convenience {@link #l(Object...)} methods to create a new instance of this class.
 * It's best practice to statically import these functions in client code.
 *
 * @param <T> the entry type
 */
public class L<T> extends C<T, Integer, T> implements OrderedSequentialS<T>, IndexedListIS<Integer, T> {
    static boolean TEST_DISABLE_HELPER_MAP_CONVERSION;
    /**
     * The {@link List} wrapped by this API.
     */
    public final List<T> l;
    
    L(List<T> list) {
        this.l = new ArrayList<>(list);
    }
    
    /**
     * Creates an empty {@link L}.
     * 
     * @see #l(Object...)
     */
    public static <T> L<T> l() {
        return new L<>(new ArrayList<>());
    }
    
    /**
     * Creates a new {@link L}. Use {@link #L(Collection)} to create a {@link L} out of an existing Collection rather than a nested {@link L}.
     */
    public static <T> L<T> l(T... ts) {
        return L(C.toStream(ts));
    }
    
    /**
     * @see #l(Object...)
     */
    public static <T> L<L<T>> l(L<T>... lists) {
        return L(Arrays.asList(lists).stream());
    }
    
    /**
     * @see #l(Object...)
     */
    public static <T> L<T> L(Stream<T> stream) {
        return new L(stream.collect(Collectors.toList()));
    }
    
    /**
     * Turns the collection(s) provided into an {@link L} rather than creating a nested {@link L}.
     */
    public static <T> L<T> L(Collection<T> collection) {
        return L(C.toStream(collection));
    }
    
    /**
     * @see #L(Collection)
     */
    public static <T> L<List<T>> L(L<T>... lists) {
        return L.L(Arrays.asList(lists).stream().map(it -> it.l).collect(Collectors.toList()));
    }
    
    /**
     * Creates an empty {@link L} of the type provided.
     * 
     * @see #l(L...)
     */
    public static <T> L<T> l(Class<T> clazz) {
        return l();
    }
    
    /**
     * Creates a {@link List}. Shorthand for {@link #l(L...)} followed by {@link #l}.
     */
    public static <T> List<T> list(T... ts) {
        return l(ts).l;
    }
    
    /**
     * Creates a {@link L} by invoking the <code>generator</code> function provided <i><code>times</code></i> times with the current index.
     */
    public static <T> L<T> l(int times, Function<Integer, T> generator) {
        return L(R.r(0).to(times).r.boxed().map(generator));
    }
    
    /**
     * Like {@link #l(int, Function)}, but turning the result into a {@link L}.
     */
    public static <T> List<T> list(int times, Function<Integer, T> generator) {
        return l(times, generator).l;
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
            return toInternalMap();
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
        if (containsAnyKey(index)) {
            return l.get(index);
        }
        else {
            return defaultFunction.apply(index);
        }
    }
    
    @Override
    public <VN extends T> VN getOrDefault(Integer key, VN defaultValue) {
        if (containsAnyKey(key)) {
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
    public Set<E<Integer, T>> getEntries() {
        if (!isParallel()) {
            // use fast sequential counting
            return Map(new Function<T, E<Integer, T>>() {
                int i = 0;
                @Override
                public E<Integer, T> apply(T t) {
                    return M.e(i++, t);
                }
            }).toSet();
        }
        else {
            // retrieve each index individually
            return Map(new Function<T, E<Integer, T>>() {
                @Override
                public E<Integer, T> apply(T t) {
                    return M.e(indexOf(t), t);
                }
            }).toSet();
        }
    }
    
    
    
    @Override
    public boolean containsAnyKey(Integer... keys) {
        return C.toStream(keys).anyMatch(it -> it >= 0 && size() > it);
    }

    @Override
    public boolean containsAnyKey(Collection<Integer>... key) {
        return C.toStream(key).anyMatch(c -> C.toStream(c).anyMatch(it -> it >= 0 && size() > it));
    }
    
    @Override
    public boolean containsAnyValue(T... values) {
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
        if (containsAnyKey(key) && currentValue == null) {
            set(key, value);
            return currentValue;
        }
        else {
            return currentValue;
        }
    }

    @Override
    public Map<Integer, T> insert(Integer index, T element) {
        if (containsAnyKey(index)) {
            throw new IndexAlreadyPresentException(index, l.get(index));
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
        if (!containsAnyKey(key)) {
            return null;
        }
        T currentValue = get(key);
        set(key, value);
        return currentValue;
    }

    @Override
    public boolean replace(Integer key, T oldValue, T newValue) {
        if (containsAnyKey(key) && l.get(key).equals(oldValue)) {
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
    public Map<Integer, T> deleteAllKeys(SequentialI<? extends Integer>... keys) {
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
    public Map<Integer, T> deleteAllValues(SequentialI<? extends T>... values) {
        C.toStream(values).forEach(c -> C.toStream(c.toCollection()).forEach(it -> deleteValue(it)));
        return toInternalMap();
    }

    @Override
    public void forEach(BiConsumer<Integer, T> action) {
        stream().forEach(consumer(action));
    }

    @Override
    public <R> List<R> map(BiFunction<Integer, T, R> function) {
        return C.toList(stream().map(function(function)));
    }

    @Override
    public <RK, RV> Map<RK, RV> mapEntries(BiFunction<Integer, T, E<RK, RV>> function) {
        return stream().map(function(function)).collect(Collectors.toMap(it -> it.k, it -> it.v));
    }

    @Override
    public E<Integer, T> find(BiPredicate<Integer, T> predicate) {
        Optional<M.E<Integer, T>> found = stream()
                .map(function((index, it) -> e(index, it)))
                .filter(F.predicate(predicate)).findFirst();
        if (found.isPresent()) {
            return found.get();
        }
        else {
            return null;
        }
    }

    @Override
    public Map<Integer, T> findAll(BiPredicate<Integer, T> predicate) {
        return stream()
                .map(function((index, it) -> e(index, it)))
                .filter(F.predicate(predicate))
                .collect(Collectors.toMap(it -> it.k, it -> it.v));
    }

    @Override
    public E<Integer, T> min(BiFunction<Integer, T, Comparable>... keyExtractors) {
        return Collections.min(stream()
                .map(function((index, it) -> e(index, it))).collect(Collectors.toList()), 
                compareAsc(C.map(keyExtractors, it -> F.function(it))));
    }

    @Override
    public E<Integer, T> max(BiFunction<Integer, T, Comparable>... keyExtractors) {
        return Collections.max(stream()
                .map(function((index, it) -> e(index, it))).collect(Collectors.toList()), 
                compareAsc(C.map(keyExtractors, it -> F.function(it))));
    }
    
    @Override
    public int count(BiPredicate<Integer, T> predicate) {
        return findAll(predicate(predicate)).size();
    }

    @Override
    public boolean every(BiPredicate<Integer, T> predicate) {
        return stream().allMatch(predicate(predicate));
    }

    @Override
    public boolean some(BiPredicate<Integer, T> predicate) {
        return stream().anyMatch(predicate(predicate));
    }

    @Override
    public boolean none(BiPredicate<Integer, T> predicate) {
        return stream().noneMatch(predicate(predicate));
    }
    
    private <R> Function<T, R> function(BiFunction<Integer, T, R> function) {
        Function<T, R> f;
        if (!isParallel()) {
            // use fast sequential counting
            f = new Function<T, R>() {
                int i = 0;
                @Override
                public R apply(T t) {
                    return function.apply(i++, t);
                }
            };
        }
        else {
            // retrieve each index individually
            f = new Function<T, R>() {
                List<T> list = stream().collect(Collectors.toList());
                @Override
                public R apply(T t) {
                    return function.apply(list.indexOf(t), t);
                }
            };
        }
        return f;
    }
    
    private Consumer<T> consumer(BiConsumer<Integer, T> action) {
        Consumer<T> f;
        if (!isParallel()) {
            // use fast sequential counting
            f = new Consumer<T>() {
                int i = 0;
                @Override
                public void accept(T t) {
                    action.accept(i++, t);
                }
            };
        }
        else {
            // retrieve each index individually
            f = new Consumer<T>() {
                List<T> list = stream().collect(Collectors.toList());
                @Override
                public void accept(T t) {
                    action.accept(list.indexOf(t), t);
                }
            };
        }
        return f;
    }
    
    private Predicate<T> predicate(BiPredicate<Integer, T> predicate) {
        Predicate<T> f;
        if (!isParallel()) {
            // use fast sequential counting
            f = new Predicate<T>() {
                int i = 0;
                @Override
                public boolean test(T t) {
                    return predicate.test(i++, t);
                }
            };
        }
        else {
            // retrieve each index individually
            f = new Predicate<T>() {
                List<T> list = stream().collect(Collectors.toList());
                @Override
                public boolean test(T t) {
                    return predicate.test(list.indexOf(t), t);
                }
            };
        }
        return f;
    }

    @Override
    public double sum(ToDoubleBiFunction<? super Integer, ? super T> mapper) {
        ToDoubleFunction<T> function;
        if (!isParallel()) {
            // use fast sequential counting
            function = new ToDoubleFunction<T>() {
                int i = 0;
                @Override
                public double applyAsDouble(T t) {
                    return mapper.applyAsDouble(i++, t);
                }
            };
        }
        else {
            // retrieve each index individually
            function = new ToDoubleFunction<T>() {
                List<T> list = stream().collect(Collectors.toList());
                @Override
                public double applyAsDouble(T t) {
                    return mapper.applyAsDouble(list.indexOf(t), t);
                }
            };
        }
        return stream().collect(Collectors.summingDouble(function));
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
    public L<T> DeleteAllValues(SequentialI<? extends T>... values) {
        return (L<T>) IndexedListIS.super.DeleteAllValues(values);
    }

    @Override
    public L<T> DeleteAllValues(Collection<? extends T>... values) {
        return (L<T>) IndexedListIS.super.DeleteAllValues(values);
    }

    @Override
    public L<T> DeleteValue(T... value) {
        return (L<T>) IndexedListIS.super.DeleteValue(value);
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

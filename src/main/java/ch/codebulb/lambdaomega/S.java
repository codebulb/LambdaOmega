package ch.codebulb.lambdaomega;

import ch.codebulb.lambdaomega.abstractions.SequentialI;
import ch.codebulb.lambdaomega.abstractions.SequentialIS;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The "S" stands for "set". An implementation of a wrapper API for {@link Set} which provides sequential access (of type <code>T</code>).<p/>
 * 
 * The constructor of this class is not visible; use one of the convenience {@link #s(Object...)} methods to create a new instance of this class.
 * It's best practice to statically import these functions in client code.
 *
 * @param <T> the entry type
 */
public class S<T> extends C<T, Integer, T> implements SequentialIS<T> {
    /**
     * The {@link Set} wrapped by this API.
     */
    public final Set<T> s;
    
    S(Set<T> set) {
        this.s = new LinkedHashSet<>(set);
    }
    
    /**
     * Creates an empty {@link S}.
     * 
     * @see #s(Object...)
     */
    public static <T> S<T> s() {
        return new S<>(new LinkedHashSet<>());
    }
    
    /**
     * Creates a new {@link S}. Use {@link #S(Collection)} to create a {@link S} out of an existing Collection rather than a nested {@sink S}.
     */
    public static <T> S<T> s(T... ts) {
        return S(C.toStream(ts));
    }
    
    /**
     * @see #s(Object...)
     */
    public static <T> S<S<T>> s(S<T>... lists) {
        return S(Arrays.asList(lists).stream());
    }
    
    /**
     * @see #s(Object...)
     */
    public static <T> S<T> S(Stream<T> stream) {
        return new S(stream.collect(Collectors.toSet()));
    }
    
    /**
     * Turns the collection(s) provided into an {@link S} rather than creating a nested {@link S}.
     */
    public static <T> S<T> S(Collection<T> collection) {
        return S(C.toStream(collection));
    }
    
    /**
     * @see #S(Collection)
     */
    public static <T> S<Set<T>> S(S<T>... sets) {
        return S.S(Arrays.asList(sets).stream().map(it -> it.s).collect(Collectors.toSet()));
    }
    
    /**
     * Creates an empty {@link S} of the type provided.
     * 
     * @see #s(S...)
     */
    public static <T> S<T> s(Class<T> clazz) {
        return s();
    }
    
    /**
     * Creates a {@link Set} consisting of the entries provided.
     */
    public static <T> Set<T> set(T... ts) {
        return C.toSet(ts);
    }
    
    /**
     * Creates a {@link S} by invoking the <code>generator</code> function provided <i><code>times</code></i> times with the current index.
     */
    public static <T> S<T> s(int times, Function<Integer, T> generator) {
        return S(R.r(0).to(times).r.boxed().map(generator));
    }
    
    /**
     * Like {@link #s(int, Function)}, but turning the result into a {@link Set}.
     */
    public static <T> Set<T> set(int times, Function<Integer, T> generator) {
        return s(times, generator).s;
    }

    @Override
    public Collection toCollection() {
        return toSet();
    }

    @Override
    public Set<T> toSet() {
        return s;
    }
    
    @Override
    public <R> S<R> Map(Function<T, R> function) {
        return S(map(function));
    }

    @Override
    public <N> S<N> Flatten() {
        return S(flatten());
    }

    @Override
    public <N> S<N> FlattenDeep() {
        return S(flattenDeep());
    }

    @Override
    public S<T> FindAll(Predicate<T> predicate) {
        return S(findAll(predicate));
    }

    @Override
    public S<T> Filter(Predicate<T> predicate) {
        return S(filter(predicate));
    }

    @Override
    public S<T> Reject(Predicate<T> predicate) {
        return S(reject(predicate));
    }

    @Override
    // Use Set as the return type for operation on entries because entries are kept in a Set.
    public <R> Collector<R, ?, Set<R>> createCollector() {
        return Collectors.toSet();
    }
    
    @Override
    public <R> Set<R> map(Function<T, R> function) {
        return (Set<R>)SequentialIS.super.map(function);
    }
    
    @Override
    public <N> Set<N> flatten() {
        return (Set<N>)SequentialIS.super.flatten();
    }

    @Override
    public <N> Set<N> flattenDeep() {
        return (Set<N>)SequentialIS.super.flattenDeep();
    }

    @Override
    public Set<T> findAll(Predicate<T> predicate) {
        return (Set<T>)SequentialIS.super.findAll(predicate);
    }
    
    @Override
    public Set<T> filter(Predicate<T> predicate) {
        return (Set<T>)SequentialIS.super.filter(predicate);
    }
    
    @Override
    public Set<T> reject(Predicate<T> predicate) {
        return (Set<T>)SequentialIS.super.reject(predicate);
    }
    
    @Override
    public S<T> R(SequentialI<? extends T>... c) {
        return (S<T>) SequentialIS.super.R(c);
    }

    @Override
    public S<T> RemoveAll(SequentialI<? extends T>... c) {
        return (S<T>) SequentialIS.super.RemoveAll(c);
    }

    @Override
    public S<T> R(Collection<? extends T>... c) {
        return (S<T>) SequentialIS.super.R(c);
    }

    @Override
    public S<T> RemoveAll(Collection<? extends T>... c) {
        return (S<T>) SequentialIS.super.RemoveAll(c);
    }

    @Override
    public S<T> Remove(T... value) {
        return (S<T>) SequentialIS.super.Remove(value);
    }

    @Override
    public S<T> A(SequentialI<? extends T>... c) {
        return (S<T>) SequentialIS.super.A(c);
    }

    @Override
    public S<T> AddAll(SequentialI<? extends T>... c) {
        return (S<T>) SequentialIS.super.AddAll(c);
    }

    @Override
    public S<T> A(Collection<? extends T>... c) {
        return (S<T>) SequentialIS.super.A(c);
    }

    @Override
    public S<T> AddAll(Collection<? extends T>... c) {
        return (S<T>) SequentialIS.super.AddAll(c);
    }

    @Override
    public S<T> Add(T... e) {
        return (S<T>) SequentialIS.super.Add(e);
    }
	
    @Override
    public S<T> r(T... value) {
        return (S<T>) SequentialIS.super.r(value);
    }

    @Override
    public S<T> a(T... e) {
        return (S<T>) SequentialIS.super.a(e);
    }

    @Override
    public S<T> Seq() {
        return (S<T>) super.Seq();
    }

    @Override
    public S<T> Sequential() {
        return (S<T>) super.Sequential();
    }

    @Override
    public S<T> Par() {
        return (S<T>) super.Par();
    }

    @Override
    public S<T> Parallel() {
        return (S<T>) super.Parallel();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.s);
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
        final S<?> other = (S<?>) obj;
        if (!Objects.equals(this.s, other.s)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "S" + s.toString();
    }
}

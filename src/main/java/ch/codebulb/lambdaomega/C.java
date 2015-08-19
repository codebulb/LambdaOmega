
package ch.codebulb.lambdaomega;

import ch.codebulb.lambdaomega.abstractions.OmegaObject;
import ch.codebulb.lambdaomega.abstractions.ReadonlyIndexedI;
import ch.codebulb.lambdaomega.abstractions.StreamableI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The "C" stands for "common collection". This is the base class of all LambdaOmega collections which implements common functionality.<p>
 * 
 * It also provides common static helper methods for collections.
 *
 * @param <T> the type of an entry when accessed sequentially
 * @param <K> the type of a key for indexed access
 * @param <V> the type of a value for indexed access
 */
// TODO Complete implementation with more converters
public abstract class C<T, K, V> extends OmegaObject implements StreamableI, ReadonlyIndexedI<K, V> {
    private boolean parallel;
    protected Function<K, V> defaultValue;
    
    public abstract Collection<T> toCollection();
    
    public Stream<T> stream() {
        return isParallel() ? toCollection().parallelStream() : toCollection().stream();
    }
    
    @Override
    public boolean isParallel() {
        return parallel;
    }
    
    public C<T, K, V> Parallel() {
        parallel = true;
        return this;
    }
    
    public C<T, K, V> Par() {
        return Parallel();
    }
    
    public C<T, K, V> Sequential() {
        parallel = false;
        return this;
    }
    
    public C<T, K, V> Seq() {
        return Sequential();
    }
    
    @Override
    public C<T, K, V> WithDefault(Function<K, V> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    
    public <C extends Collection<T>> C to(Class<C> format) {
        return to(stream(), format);
    }
    
    public static <T, C extends Collection<T>> C to(Stream<T> stream, Class<C> format) {
        return (C)stream.collect(Collectors.toCollection(() -> {
            if (format.isAssignableFrom(ArrayList.class)) {
                return new ArrayList<T>();
            }
            if (format.isAssignableFrom(LinkedList.class)) {
                return new LinkedList<T>();
            }
            throw new IllegalArgumentException("Return type not supported: " + format);
        }));
    }
    
    public static <T> Stream<T> toStream(Collection<T> collection) {
        return StreamSupport.stream(collection.spliterator(), false);
    }
    
    public static <T> Stream<T> toStream(T... ts) {
        return Arrays.stream(ts);
    }
    
    public static <T> Stream<T> toStream(M map) {
        return map.stream();
    }
    
    public static <T> List<T> toList(Collection<T> collection) {
        return new ArrayList<>(collection);
    }
    
    public static <T> List<T> toList(T... ts) {
        return Arrays.asList(ts);
    }
    
    public static <T> List<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toList());
    }
    
    public static <T> Set<T> toSet(Collection<T> collection) {
        return new HashSet<>(collection);
    }
    
    public static <T> Set<T> toSet(T... ts) {
        return toStream(ts).collect(Collectors.toSet());
    }
    
    public static <T, R> List<R> map(T[] ts, Function<T, R> function) {
        return C.toStream(ts).map(function).collect(Collectors.toList());
    }
    
    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> function) {
        return C.toStream(collection).map(function).collect(Collectors.toList());
    }
}

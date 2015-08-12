package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An abstraction for a sequentially accessed data structure. This interface makes no assumptions about the data structure's ordering.
 * It is loosely inspired by {@link Collection}.
 */
public interface SequentialI<T> {
    public Collection<T> toCollection();
    
    public default List<T> toList() {
        return C.toList(toCollection());
    }
    
    public default Set<T> toSet() {
        return C.toSet(toCollection());
    }
    
    public default Stream<T> stream() {
        return toCollection().stream();
    }
    
    public default Stream<T> parallelStream() {
        return toCollection().parallelStream();
    }
    
    public default Iterator<T> iterator() {
        return toCollection().iterator();
    }
    
    public default Spliterator<T> spliterator() {
        return toCollection().spliterator();
    }

    public default Object[] toArray() {
        return toCollection().toArray();
    }
    
    public default <T> T[] toArray(T[] a) {
        return toCollection().toArray(a);
    }
    
    public default T[] toArray(IntFunction<T[]> generator) {
        return stream().toArray(generator);
    }
    
    public default int size() {
        return toCollection().size();
    }
    
    public default boolean isEmpty() {
        return toCollection().isEmpty();
    }
    
    public default boolean contains(T... o) {
        return toCollection().containsAll(C.toStream(o).collect(Collectors.toList()));
    }
    
    public default boolean containsAll(Collection<? extends T>... c) {
        return C.toStream(c).allMatch(it -> toCollection().containsAll(it));
    }
    
    public default List<T> clear() {
        toCollection().clear();
        return toList();
    }

    public default List<T> remove(T... value) {
        C.toStream(value).forEach(it -> toCollection().remove(it));
        return toList();
    }
    
    public default List<T> removeAll(Collection<? extends T>... c) {
        C.toStream(c).forEach(it -> toCollection().removeAll(it));
        return toList();
    }
    
    public default List<T> retainAll(Collection<? extends T>... c) {
        C.toStream(c).forEach(it -> toCollection().retainAll(it));
        return toList();
    }
}

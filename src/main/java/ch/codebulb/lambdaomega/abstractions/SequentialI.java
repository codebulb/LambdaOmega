package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An abstraction for a sequentially accessed data structure. This interface makes no assumptions about the data structure's ordering.
 * It is loosely inspired by {@link Collection}.
 */
public interface SequentialI<T> {
    public Collection<T> toCollection();
    
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
    
    public default Collection<T> clear() {
        toCollection().clear();
        return toCollection();
    }
    
    public default Collection<T> add(T... e) {
        C.toStream(e).forEach(it -> toCollection().add(it));
        return toCollection();
    }
    
    public default Collection<T> addAll(Collection<? extends T>... c) {
        C.toStream(c).forEach(it -> toCollection().addAll(it));
        return toCollection();
    }

    public default Collection<T> remove(T... value) {
        C.toStream(value).forEach(it -> toCollection().remove(it));
        return toCollection();
    }
    
    public default Collection<T> removeAll(Collection<? extends T>... c) {
        C.toStream(c).forEach(it -> toCollection().removeAll(it));
        return toCollection();
    }
    
    public default Collection<T> retainAll(Collection<? extends T>... c) {
        List<T> all = new ArrayList<>();
        C.toStream(c).forEach(it -> all.addAll(it));
        
        toCollection().retainAll(all);
        return toCollection();
    }
}

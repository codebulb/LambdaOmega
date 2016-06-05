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
    /**
     * Gets the wrapped {@link Collection} or, if not backed by a {@link Collection}, 
     * returns the wrapped objects after being transformed into a {@link Collection}
     */
    public Collection<T> toCollection();
    
    public default List<T> toList() {
        return C.toList(toCollection());
    }
    
    public default Set<T> toSet() {
        return C.toSet(toCollection());
    }
    
    /**
     * @see Collection#stream()
     */
    public default Stream<T> stream() {
        return toCollection().stream();
    }
    
    /**
     * @see Collection#parallelStream()
     */
    public default Stream<T> parallelStream() {
        return toCollection().parallelStream();
    }
    
    /**
     * @see Collection#iterator()
     */
    public default Iterator<T> iterator() {
        return toCollection().iterator();
    }
    
    /**
     * @see Collection#spliterator()
     */
    public default Spliterator<T> spliterator() {
        return toCollection().spliterator();
    }
    
    /**
     * @see Collection#size()
     */
    public default int size() {
        return toCollection().size();
    }
    
    /**
     * @see Collection#isEmpty()
     */
    public default boolean isEmpty() {
        return toCollection().isEmpty();
    }
    
    /**
     * @see Collection#containsAll(Collection)
     */
    public default boolean contains(T... o) {
        return toCollection().containsAll(C.toStream(o).collect(Collectors.toList()));
    }
    
    /**
     * @see Collection#containsAll(Collection)
     */
    public default boolean containsAll(Collection<? extends T>... c) {
        return C.toStream(c).allMatch(it -> toCollection().containsAll(it));
    }
    
    public default Collection<T> clear() {
        toCollection().clear();
        return toCollection();
    }
    
    /**
     * @see Collection#add(Object)
     */
    public default Collection<T> add(T... e) {
        C.toStream(e).forEach(it -> toCollection().add(it));
        return toCollection();
    }
    
    /**
     * @see Collection#addAll(Collection)
     */
    public default Collection<T> addAll(Collection<? extends T>... c) {
        C.toStream(c).forEach(it -> toCollection().addAll(it));
        return toCollection();
    }

    /**
     * @see Collection#remove(Object)
     */
    public default Collection<T> remove(T... value) {
        C.toStream(value).forEach(it -> toCollection().remove(it));
        return toCollection();
    }
    
    /**
     * @see Collection#removeAll(Collection)
     */
    public default Collection<T> removeAll(Collection<? extends T>... c) {
        C.toStream(c).forEach(it -> toCollection().removeAll(it));
        return toCollection();
    }
    
    /**
     * @see Collection#retainAll(Collection)
     */
    public default Collection<T> retainAll(Collection<? extends T>... c) {
        List<T> all = new ArrayList<>();
        C.toStream(c).forEach(it -> all.addAll(it));
        
        toCollection().retainAll(all);
        return toCollection();
    }
}

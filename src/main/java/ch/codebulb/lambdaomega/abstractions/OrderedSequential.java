package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import static ch.codebulb.lambdaomega.L.l;
import ch.codebulb.lambdaomega.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Adds a contract for access to an ordered data structure to a {@link SequentialIS}.
 */
public interface OrderedSequential<T> extends SequentialIS<T> {
    /**
     * Gets the wrapped {@link List} or, if not backed by a {@link List}, 
     * returns the wrapped objects after being transformed into a {@link List}
     */
    public default List<T> toList() {
        return C.toList(toCollection());
    }
    
    /**
     * @see Collection#toArray()
     */
    public default Object[] toArray() {
        return toCollection().toArray();
    }
    
    /**
     * @see Collection#toArray(Object[])
     */
    public default <T> T[] toArray(T[] a) {
        return toCollection().toArray(a);
    }
    
    /**
     * @see Stream#toArray(IntFunction)
     */
    public default T[] toArray(IntFunction<T[]> generator) {
        return stream().toArray(generator);
    }
    
    /**
     * Returns an int-{@link R} ranging from 0 (incl.) to the number of elements (excl.)
     */
    public default R toRange() {
        return R.r(0).to(toCollection().size());
    }
    
    /**
     * @see List#lastIndexOf(Object)
     */
    public default int lastIndexOf(T o) {
        return toList().lastIndexOf(o);
    }

    @Deprecated
    public default ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @see List#subList(int, int)
     */
    public default List<T> subList(int fromIndex, int toIndex) {
        return toList().subList(fromIndex, toIndex);
    }
    
    /**
     * Returns a {@link List} with the elements covered by the <code>range</code> provided.
     */
    public default List<T> get(R range) {
        return new ArrayList<>(toList().subList(range.startInclusive, range.endExclusive+2));
    }
    
    /**
     * @see List#addAll(int, Collection)
     */
    public default List<T> addAt(int index, T... element) {
        toList().addAll(index, C.toList(element));
        return toList();
    }

    /**
     * @see List#addAll(int, Collection)
     */
    public default List<T> addAllAt(int index, Collection<? extends T>... c) {
        l(c).forEach((i, it) -> toList().addAll(i, it));
        return toList();
    }

    @Override
    // Use List as the return type for operation on entries because entries are kept in a List.
    default <R> Collector<R, ?, List<R>> createCollector() {
        return Collectors.toList();
    }

    @Override
    public default <R> List<R> map(Function<T, R> function) {
        return (List<R>)SequentialIS.super.map(function);
    }

    @Override
    public default List<T> findAll(Predicate<T> predicate) {
        return (List<T>)SequentialIS.super.findAll(predicate);
    }
    
    @Override
    public default List<T> filter(Predicate<T> predicate) {
        return (List<T>)SequentialIS.super.filter(predicate);
    }
    
    @Override
    public default List<T> reject(Predicate<T> predicate) {
        return (List<T>)SequentialIS.super.reject(predicate);
    }

    @Override
    public default <K> Map<K, List<T>> groupBy(Function<? super T, ? extends K> classifier) {
        return (Map<K, List<T>>)SequentialIS.super.groupBy(classifier);
    }
    
    @Override
    public default Map<Boolean, ? extends Collection<T>> partition(Predicate<? super T> predicate) {
        return (Map<Boolean, List<T>>)SequentialIS.super.partition(predicate);
    }
    
    @Override
    public default List<T> removeAll(SequentialI<? extends T>... c) {
        return (List<T>)SequentialIS.super.removeAll(c);
    }
    
    @Override
    public default List<T> clear() {
        return (List<T>)SequentialIS.super.clear();
    }
    
    @Override
    public default List<T> remove(T... value) {
        return (List<T>)SequentialIS.super.remove(value);
    }
    
    @Override
    public default List<T> removeAll(Collection<? extends T>... c) {
        return (List<T>)SequentialIS.super.removeAll(c);
    }
    
    @Override
    public default List<T> retainAll(Collection<? extends T>... c) {
        return (List<T>)SequentialIS.super.retainAll(c);
    }
    
    @Override
    public default List<T> add(T... e) {
        return (List<T>)SequentialIS.super.add(e);
    }
    
    @Override
    public default List<T> addAll(Collection<? extends T>... c) {
        return (List<T>)SequentialIS.super.addAll(c);
    }

    @Override
    public default List<T> addAll(SequentialI<? extends T>... c) {
        return (List<T>)SequentialIS.super.addAll(c);
    }
}

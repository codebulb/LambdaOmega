package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import static ch.codebulb.lambdaomega.L.l;
import ch.codebulb.lambdaomega.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Adds a contract for access to an ordered data structure to a {@link SequentialIS}.
 */
public interface OrderedSequential<T> extends SequentialIS<T> {
    public default int lastIndexOf(T o) {
        return toList().lastIndexOf(o);
    }

    public default ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public default List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public default R toRange() {
        return R.r(0).to(toCollection().size());
    }
    
    public default List<T> get(R range) {
        return new ArrayList<>(toList().subList(range.startInclusive, range.endExclusive+2));
    }
    
    public default List<T> addAt(int index, T... element) {
        toList().addAll(index, C.toList(element));
        return toList();
    }

    public default List<T> addAllAt(int index, Collection<? extends T>... c) {
        l(c).forEach((i, it) -> toList().addAll(i, it));
        return toList();
    }

    @Override
    // Use List as the return type for operation on entries because entries are kept in a List.
    public default <R> Collector<R, ?, List<R>> createCollector() {
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
    
}

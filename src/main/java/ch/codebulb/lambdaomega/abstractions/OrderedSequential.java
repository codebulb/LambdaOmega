package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import static ch.codebulb.lambdaomega.L.l;
import ch.codebulb.lambdaomega.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

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
}

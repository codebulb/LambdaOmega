package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import static ch.codebulb.lambdaomega.L.l;
import java.util.Collection;
import java.util.List;

/**
 * Adds a contract to work with "add" / "insert" functionality to a {@link SequentialI}.
 */
public interface SequentialIAdd<T> extends SequentialI<T> {
    public default List<T> add(T... e) {
        C.toStream(e).forEach(it -> toCollection().add(it));
        return toList();
    }
    
    public default List<T> addAll(Collection<? extends T>... c) {
        C.toStream(c).forEach(it -> toCollection().addAll(it));
        return toList();
    }
    
    public default SequentialIAdd<T> Add(T... e) {
        add(e);
        return this;
    }
    
    public default SequentialIAdd<T> a(T... e) {
        return Add(e);
    }
    
    public default SequentialIAdd<T> AddAll(Collection<? extends T>... c) {
        addAll(c);
        return this;
    }
    
    public default SequentialIAdd<T> A(Collection<? extends T>... c) {
        return AddAll(c);
    }
    
    public default List<T> addAll(SequentialI<? extends T>... c) {
        C.toStream(c).forEach(it -> toCollection().addAll(it.toCollection()));
        return toList();
    }
    
    public default SequentialIAdd<T> AddAll(SequentialI<? extends T>... c) {
        addAll(c);
        return this;
    }
    
    public default SequentialIAdd<T> A(SequentialI<? extends T>... c) {
        return AddAll(c);
    }
}

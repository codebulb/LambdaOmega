package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import java.util.Collection;

/**
 * Adds a contract to work with "add" / "insert" functionality to a {@link SequentialI}.
 */
public interface SequentialIAdd<T> extends SequentialI<T> {
    public default Collection<T> add(T... e) {
        C.toStream(e).forEach(it -> toCollection().add(it));
        return toCollection();
    }
    
    public default Collection<T> addAll(Collection<? extends T>... c) {
        C.toStream(c).forEach(it -> toCollection().addAll(it));
        return toCollection();
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
    
    public default Collection<T> addAll(SequentialI<? extends T>... c) {
        C.toStream(c).forEach(it -> toCollection().addAll(it.toCollection()));
        return toCollection();
    }
    
    public default SequentialIAdd<T> AddAll(SequentialI<? extends T>... c) {
        addAll(c);
        return this;
    }
    
    public default SequentialIAdd<T> A(SequentialI<? extends T>... c) {
        return AddAll(c);
    }
}

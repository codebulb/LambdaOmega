package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import java.util.Collection;

/**
 * Adds syntactic sugar to a {@link SequentialIFunctionsS}.
 */
public interface SequentialIS<T> extends SequentialIFunctionsS<T> {
    public default SequentialI<T> Add(T... e) {
        add(e);
        return this;
    }
    
    public default SequentialI<T> a(T... e) {
        return Add(e);
    }
    
    public default SequentialI<T> AddAll(Collection<? extends T>... c) {
        addAll(c);
        return this;
    }
    
    public default SequentialI<T> A(Collection<? extends T>... c) {
        return AddAll(c);
    }
    
    public default Collection<T> addAll(SequentialI<? extends T>... c) {
        C.toStream(c).forEach(it -> toCollection().addAll(it.toCollection()));
        return toCollection();
    }
    
    public default SequentialI<T> AddAll(SequentialI<? extends T>... c) {
        addAll(c);
        return this;
    }
    
    public default SequentialI<T> A(SequentialI<? extends T>... c) {
        return AddAll(c);
    }
    
    public default SequentialIS<T> Remove(T... value) {
        remove(value);
        return this;
    }
    
    public default SequentialIS<T> r(T... value) {
        return Remove(value);
    }
    
    public default SequentialIS<T> RemoveAll(Collection<? extends T>... c) {
        removeAll(c);
        return this;
    }
    
    public default SequentialIS<T> R(Collection<? extends T>... c) {
        return RemoveAll(c);
    }
    
    public default Collection<T> removeAll(SequentialI<? extends T>... c) {
        C.toStream(c).forEach(it -> removeAll(it.toCollection()));
        return toCollection();
    }
    
    public default SequentialIS<T> RemoveAll(SequentialI<? extends T>... c) {
        removeAll(c);
        return this;
    }
    
    public default SequentialIS<T> R(SequentialI<? extends T>... c) {
        return RemoveAll(c);
    }
}

package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import java.util.Collection;

/**
 * Adds syntactic sugar to a {@link SequentialIFunctionsS}.
 */
public interface SequentialIS<T> extends SequentialIFunctionsS<T> {
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

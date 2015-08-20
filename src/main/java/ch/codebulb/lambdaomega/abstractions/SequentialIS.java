package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.C;
import java.util.Collection;

/**
 * Adds syntactic sugar to a {@link SequentialIFunctionsS}.
 */
public interface SequentialIS<T> extends SequentialIFunctionsS<T> {
    /**
     * @see #add(Object...)
     */
    public default SequentialI<T> Add(T... e) {
        add(e);
        return this;
    }
    
    /**
     * @see #add(Object...)
     */
    public default SequentialI<T> a(T... e) {
        return Add(e);
    }
    
    /**
     * @see #addAll(Collection...)
     */
    public default SequentialI<T> AddAll(Collection<? extends T>... c) {
        addAll(c);
        return this;
    }
    
    /**
     * @see #addAll(Collection...)
     */
    public default SequentialI<T> A(Collection<? extends T>... c) {
        return AddAll(c);
    }
    
    /**
     * @see #addAll(Collection...)
     */
    public default Collection<T> addAll(SequentialI<? extends T>... c) {
        C.toStream(c).forEach(it -> toCollection().addAll(it.toCollection()));
        return toCollection();
    }
    
    /**
     * @see #addAll(Collection...)
     */
    public default SequentialI<T> AddAll(SequentialI<? extends T>... c) {
        addAll(c);
        return this;
    }
    
    /**
     * @see #addAll(Collection...)
     */
    public default SequentialI<T> A(SequentialI<? extends T>... c) {
        return AddAll(c);
    }
    
    /**
     * @see #remove(Object...)
     */
    public default SequentialIS<T> Remove(T... value) {
        remove(value);
        return this;
    }
    
    /**
     * @see #remove(Object...)
     */
    public default SequentialIS<T> r(T... value) {
        return Remove(value);
    }
    
    /**
     * @see #removeAll(Collection...)
     */
    public default SequentialIS<T> RemoveAll(Collection<? extends T>... c) {
        removeAll(c);
        return this;
    }
    
    /**
     * @see #removeAll(Collection...)
     */
    public default SequentialIS<T> R(Collection<? extends T>... c) {
        return RemoveAll(c);
    }
    
    /**
     * @see #removeAll(Collection...)
     */
    public default Collection<T> removeAll(SequentialI<? extends T>... c) {
        C.toStream(c).forEach(it -> removeAll(it.toCollection()));
        return toCollection();
    }
    
    /**
     * @see #removeAll(Collection...)
     */
    public default SequentialIS<T> RemoveAll(SequentialI<? extends T>... c) {
        removeAll(c);
        return this;
    }
    
    /**
     * @see #removeAll(Collection...)
     */
    public default SequentialIS<T> R(SequentialI<? extends T>... c) {
        return RemoveAll(c);
    }
}

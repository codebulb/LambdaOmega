package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.list;
import ch.codebulb.lambdaomega.abstractions.OmegaObject;
import ch.codebulb.lambdaomega.abstractions.ReadonlyIndexedI;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * The "V2" stands for "2d vector". This is an implementation of a 2-dimensional vector = a 2-ary tuple.<p/>
 * 
 * The constructor of this class is not visible; use the convenience {@link #v(Object, Object)} method to create a new instance of this class.
 * It's best practice to statically import this function in client code.
 *
 * @param <X> the type of the 1st element
 * @param <Y> the type of the 2nd element
 */
public class V2<X, Y> extends OmegaObject implements ReadonlyIndexedI<Integer, Object> {
    private X x;
    private Y y;
    private Function<Integer, Object> defaultFunction;

    V2(X x, Y y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Creates a {@link V2} consisting of the value pair <code>x</code>, <code>y</code>.
     */
    public static <X, Y> V2<X, Y> v(X x, Y y) {
        return new V2(x, y);
    }
    
    /**
     * Turns this vector into a {@link LV2} with subsequent {@link LV2#a(Object, Object)} call.
     */
    public LV2<X, Y> a(X x, Y y) {
        return new LV2(list(this)).a(x, y);
    }

    public X get0() {
        return x;
    }

    public V2<X, Y> set0(X x) {
        this.x = x;
        return this;
    }

    public Y get1() {
        return y;
    }

    public V2<X, Y> set1(Y y) {
        this.y = y;
        return this;
    }

    public <T> V2<X, Y> set(int i, T value) {
        if (i == 0) {
            set0((X) value);
        }
        else {
             set1((Y) value);
        }
        return this;
    }
    
    @Override
    public <T> T get(Integer key) {
        T ret = null;
        if (key == 0) {
            ret = (T) get0();
        }
        else if (key == 1) {
            ret = (T) get1();
        }
        
        if (ret == null) {
            if (defaultFunction != null) {
                return (T) defaultFunction.apply(key);
            }
            else {
                throw new IllegalArgumentException("Illegal vector access key : " + key);
            }
        }
        else {
            return ret;
        }
    }    
    
    @Override
    public <T> T getOrDefault(Integer key, T defaultValue) {
        T ret = null;
        if (key == 0) {
            ret = (T) get0();
        }
        else if (key == 1) {
            ret = (T) get1();
        }
        
        if (ret == null) {
            return (T) defaultValue;
        }
        else {
            return ret;
        }
    }

    @Override
    public V2<X, Y> WithDefault(Function<Integer, Object> defaultValue) {
        this.defaultFunction = defaultValue;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.x);
        hash = 53 * hash + Objects.hashCode(this.y);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final V2<?, ?> other = (V2<?, ?>) obj;
        if (!Objects.equals(this.x, other.x)) {
            return false;
        }
        if (!Objects.equals(this.y, other.y)) {
            return false;
        }
        return true;
    }
    
    /**
     * A sub-type of a {@link L} of {@link V2}, providing additional support to {@link #Add(Object, Object)} a {@link V2} by individual elements.
     *
     * @param <X> the type of the 1st element
     * @param <Y> the type of the 2nd element
     */
    public static class LV2<X, Y> extends L<V2<X, Y>> {
        LV2(List<V2<X, Y>> list) {
            super(list);
        }
        
        /**
         * Creates a {@link V2} consisting of the value pair <code>x</code>, <code>y</code> and {@link #add(Object...)}s it.
         */
        public LV2<X, Y> Add(X x, Y y) {
          return (LV2<X, Y>) Add(V2.v(x, y));
        }
        
        /**
         * @see #Add(Object, Object)
         */
        public LV2<X, Y> a(X x, Y y) {
            return (LV2<X, Y>) a(V2.v(x, y));
        }
    }

    @Override
    public String toString() {
        return "(" + get0() + ", " + get1() + ")";
    }
 }

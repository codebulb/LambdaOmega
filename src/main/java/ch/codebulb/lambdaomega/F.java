package ch.codebulb.lambdaomega;

import ch.codebulb.lambdaomega.M.E;
import ch.codebulb.lambdaomega.abstractions.OmegaObject;
import ch.codebulb.lambdaomega.abstractions.ReadonlyIndexedI;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

/**
 * The "F" stands for "function". Provides a common interface for all {@link FunctionalInterface} implementations for which it acts as a wrapper.<p>
 * 
 * It also provides common static helper methods for functions.<p/>
 * 
 * The constructor of this class is not visible; use the convenience {@link #f(Function)} method to create a new instance of this class,
 * or one of the other static helper constructor functions.
 * 
 * It's best practice to statically import these functions in client code.
 *
 * @param <T> the function input type
 * @param <T1> the type of the 1st function input if it takes two inputs; must be same as <code>T</code> otherwise
 * @param <T2> the type of the 2nd function input if it takes two inputs; must be same as <code>T</code> otherwise
 * @param <R> the function return type
 */
// TODO Complete implementation, supporting all available functional interfaces
public class F<T, T1 extends T, T2 extends T, R> extends OmegaObject implements ReadonlyIndexedI<T, R> {
    private Function<T, R> function;
    private BiFunction<T1, T2, R> biFunction;
    private BiConsumer<T1, T2> biConsumer;
    private BiPredicate<T1, T2> biPredicate;
    private ToDoubleBiFunction<T1, T2> toDoubleBiFunction;
    
    private Function<T, R> defaultValue;

    F(Function<T, R> function) {
        this.function = function;
    }
    
    F(BiFunction<T1, T2, R> biFunction) {
        this.biFunction = biFunction;
    }

    F(BiConsumer<T1, T2> biConsumer) {
        this.biConsumer = biConsumer;
    }
    
    F(BiPredicate<T1, T2> biPredicate) {
        this.biPredicate = biPredicate;
    }
    
    F(ToDoubleBiFunction<T1, T2> toDoubleBiFunction) {
        this.toDoubleBiFunction = toDoubleBiFunction;
    }
    
    /**
     * Invokes the wrapped function with the <code>input</code> provided.
     */
    public R call(T input) {
        if (function != null) {
            return function.apply(input);
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }
    
    /**
     * Invokes the wrapped function with the <code>inputs</code> provided. Depending on how many parameters the actual wrapped function takes,
     * the inputs are provided in their order, with surplus parameters being discarded.
     */
    public R call(T... inputs) {
        if (biConsumer != null) {
            biConsumer.accept((T1)inputs[0], (T2)inputs[1]);
            return null;
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }
    
    /**
     * Turns a wrapped {@link BiFunction} <i>f1: (K, V) &rarr; R</i> into a {@link Function} <i>f2: ({@link E}&lt;K, V&gt;) &rarr; R</i>.
     */
    public Function<M.E<T1, T2>, R> function() {
        if (biFunction != null) {
            return (M.E<T1, T2> e) -> 
                biFunction.apply(e.k, e.v);
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }
    
    /**
     * Turns a wrapped {@link BiConsumer} <i>f1: (K, V) &rarr; <code>void</code></i> into a {@link Consumer} <i>f2: ({@link E}&lt;K, V&gt;) &rarr; <code>void</code></i>.
     */
    public Consumer<M.E<T1, T2>> consumer() {
        if (biConsumer != null) {
            return (M.E<T1, T2> e) -> 
                biConsumer.accept(e.k, e.v);
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }
    
    /**
     * Turns a wrapped {@link BiPredicate} <i>f1: (K, V) &rarr; <code>boolean</code></i> into a {@link Predicate} <i>f2: ({@link E}&lt;K, V&gt;) &rarr; <code>boolean</code></i>.
     */
    public Predicate<M.E<T1, T2>> predicate() {
        if (biPredicate != null) {
            return (M.E<T1, T2> e) -> 
                biPredicate.test(e.k, e.v);
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }
    
    /**
     * Turns a wrapped {@link ToDoubleBiFunction} <i>f1: (K, V) &rarr; <code>double</code></i> into a {@link ToDoubleFunction} <i>f2: ({@link E}&lt;K, V&gt;) &rarr; <code>double</code></i>.
     */
    public ToDoubleFunction<M.E<T1, T2>> toDoubleFunction() {
        if (toDoubleBiFunction != null) {
            return (M.E<T1, T2> e) -> 
                toDoubleBiFunction.applyAsDouble(e.k, e.v);
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }

    /**
     * Like {@link ReadonlyIndexedI#get(Object)}, but invokes {@link #call(Object)} with the <code>key</code> provided.
     */
    @Override
    public <VN extends R> VN get(T key) {
        VN ret = (VN) call(key);
        if (ret == null) {
            return (VN) defaultValue;
        }
        return ret;
    }

    /**
     * Like {@link ReadonlyIndexedI#get(Object)}, but invokes {@link #call(Object)} with the <code>key</code> provided.
     */
    @Override
    public <VN extends R> VN getOrDefault(T key, R defaultValue) {
        VN ret = (VN) call(key);
        if (ret == null) {
            return (VN) defaultValue;
        }
        return ret;
    }
    
    /**
     * Like {@link ReadonlyIndexedI#get(Object...)}, but invokes {@link #call(Object)} with each of the <code>keys</code> provided.
     */
    @Override
    public List<R> get(T... keys) {
      return ReadonlyIndexedI.super.get(keys);
    }

    @Override
    public F<T, T1, T2, R> WithDefault(Function<T, R> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    
    /**
     * Creates a {@link F} which wraps the function provided.
     */
    public static <T, R> F<T, T, T, R> f(Function<T, R> function) {
        return new F(function);
    }
    
    /**
     * @see #function()
     */
    public static <K, V, R> Function<M.E<K, V>, R> function(BiFunction<K, V, R> function) {
      return new F(function).function();
    }
    
    /**
     * @see #consumer()
     */
    public static <K, V> Consumer<M.E<K, V>> consumer(BiConsumer<K, V> function) {
        return new F(function).consumer();
    }
    
    /**
     * @see #predicate()
     */
    public static <K, V> Predicate<M.E<K, V>> predicate(BiPredicate<K, V> function) {
        return new F(function).predicate();
    }
    
    /**
     * @see #toDoubleFunction()
     */
    public static <K, V> ToDoubleFunction<M.E<? extends K, ? extends V>> toDoubleFunction(ToDoubleBiFunction<? extends K, ? extends V> function) {
        return new F(function).toDoubleFunction();
    }
    
    /**
     * Returns an <i>ascending</i> {@link Comparator} built from subsequently applying the <code>keyExtractors</code> provided.
     */
    public static <T> Comparator<T> compareAsc(Function<? super T, Comparable>... keyExtractors) {
        return compareAsc(C.toList(keyExtractors));
    }
    
    /**
     * @see #compareAsc(Function...)
     */
    public static <T> Comparator<T> compareAsc(List<Function<? super T, Comparable>> keyExtractors) {
        Comparator comparator = null;
        for (Function<? super T, Comparable> keyExtractor : keyExtractors) {
            if (comparator == null) {
                comparator = Comparator.comparing(keyExtractor);
            }
            else {
                comparator = comparator.thenComparing(keyExtractor);
            }
        }
        
        if (comparator == null) {
            return (Comparator<T>)Comparator.naturalOrder();
        }
        return comparator;
    }
    
    /**
     * Returns a {@link Comparator} built from subsequently applying the <code>keyExtractors</code> provided.
     * Every keyExtractor is a function <i>f: (T) &rarr; {@link V2}&lt;f1, Boolean&gt;</i> where the return type is 
     * a {@link V2}; its 1st element is the actual keyExtractor function <i>f1</i>, its 2nd element is a Boolean specifying the sort order for that specific
     * keyExtractor: <code>true</code> for <i>ascending</i>.
     */
    public static <T> Comparator<T> compare(Function<? super T, V2<Function<? super T, Comparable>, Boolean>>... keyExtractors) {
        Comparator<T> comparator = null;
        Comparator modificator = null;
        for (Function<? super T, V2<Function<? super T, Comparable>, Boolean>> keyExtractor : keyExtractors) {
            if (!keyExtractor.apply(null).get1()) {
                modificator = Comparator.reverseOrder();
            }
            else {
                modificator = Comparator.naturalOrder();
            }
            if (comparator == null) {
                comparator = Comparator.comparing((T it) -> keyExtractor.apply(it).get0().apply(it), modificator);
            }
            else {
                comparator = comparator.thenComparing((T it) -> keyExtractor.apply(it).get0().apply(it), modificator);
            }
        }
        
        if (comparator == null) {
            return (Comparator<T>)Comparator.naturalOrder();
        }
        return comparator;
    }
}

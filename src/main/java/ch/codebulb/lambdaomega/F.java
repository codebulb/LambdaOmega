package ch.codebulb.lambdaomega;

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
 * The "F" stands for "function". Provides a common interface for all {@link FunctionalInterface} implementations.<p>
 * 
 * It also provides common static helper methods for functions.
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

    public F(Function<T, R> function) {
        this.function = function;
    }
    
    public F(BiFunction<T1, T2, R> biFunction) {
        this.biFunction = biFunction;
    }

    public F(BiConsumer<T1, T2> biConsumer) {
        this.biConsumer = biConsumer;
    }
    
    public F(BiPredicate<T1, T2> biPredicate) {
        this.biPredicate = biPredicate;
    }
    
    public F(ToDoubleBiFunction<T1, T2> toDoubleBiFunction) {
        this.toDoubleBiFunction = toDoubleBiFunction;
    }
    
    public R call(T input) {
        if (function != null) {
            return function.apply(input);
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }
    
    public R call(T... inputs) {
        if (biConsumer != null) {
            biConsumer.accept((T1)inputs[0], (T2)inputs[1]);
            return null;
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }
    
    public Function<M.E<T1, T2>, R> function() {
        if (biFunction != null) {
            return (M.E<T1, T2> e) -> 
                biFunction.apply(e.k, e.v);
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }
    
    public Consumer<M.E<T1, T2>> consumer() {
        if (biConsumer != null) {
            return (M.E<T1, T2> e) -> 
                biConsumer.accept(e.k, e.v);
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }
    
    public Predicate<M.E<T1, T2>> predicate() {
        if (biPredicate != null) {
            return (M.E<T1, T2> e) -> 
                biPredicate.test(e.k, e.v);
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }
    
    public ToDoubleFunction<M.E<T1, T2>> toDoubleFunction() {
        if (toDoubleBiFunction != null) {
            return (M.E<T1, T2> e) -> 
                toDoubleBiFunction.applyAsDouble(e.k, e.v);
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }

    @Override
    public <VN extends R> VN get(T key) {
        VN ret = (VN) call(key);
        if (ret == null) {
            return (VN) defaultValue;
        }
        return ret;
    }

    @Override
    public <VN extends R> VN getOrDefault(T key, R defaultValue) {
        VN ret = (VN) call(key);
        if (ret == null) {
            return (VN) defaultValue;
        }
        return ret;
    }

    @Override
    public F<T, T1, T2, R> WithDefault(Function<T, R> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    
    public static <T, R> F<T, T, T, R> f(Function<T, R> function) {
        return new F(function);
    }
    
    public static <K, V> Consumer<M.E<K, V>> consumer(BiConsumer<K, V> function) {
        return new F(function).consumer();
    }
    
    public static <K, V> Predicate<M.E<K, V>> predicate(BiPredicate<K, V> function) {
        return new F(function).predicate();
    }
    
    public static <K, V, R> Function<M.E<K, V>, R> function(BiFunction<K, V, R> function) {
        return new F(function).function();
    }
    
    public static <K, V> ToDoubleFunction<M.E<? extends K, ? extends V>> toDoubleFunction(ToDoubleBiFunction<? extends K, ? extends V> function) {
        return new F(function).toDoubleFunction();
    }
    
    public static <T> Comparator<T> compareAsc(Function<? super T, Comparable>... keyExtractors) {
        return compareAsc(C.toList(keyExtractors));
    }
    
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

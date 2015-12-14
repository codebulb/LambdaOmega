package ch.codebulb.lambdaomega;

import ch.codebulb.lambdaomega.M.*;
import ch.codebulb.lambdaomega.abstractions.BiFunctionalI;
import ch.codebulb.lambdaomega.abstractions.functions.DoubleBiFunction;
import ch.codebulb.lambdaomega.abstractions.functions.IntBiFunction;
import ch.codebulb.lambdaomega.abstractions.functions.LongBiFunction;
import ch.codebulb.lambdaomega.abstractions.FunctionalI;
import ch.codebulb.lambdaomega.abstractions.OmegaObject;
import ch.codebulb.lambdaomega.abstractions.ReadonlyIndexedI;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;

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
public class F<T, T1 extends T, T2 extends T, R> extends OmegaObject implements ReadonlyIndexedI<T, R> {
    private Function<T, R> function;
    
    private BiFunction<T1, T2, R> biFunction;
    
    private Function<T, R> defaultFunction;
    
    public final FunctionalI<T, R> f;
    public final BiFunctionalI<T1, T2, R> f2;

    F(Function<T, R> function) {
        this.function = function;
        this.f = (T t) -> function.apply(t);
        this.f2 = null;
    }

    F(Consumer<T> consumer) {
        this(new Function<T, R>() {
            @Override
            public R apply(T t) {
                consumer.accept(t);
                return null;
            }
        });
    }

    F(Predicate<T> predicate) {
        this(new Function<T, R>() {
            @Override
            public R apply(T t) {
                return (R) (Boolean)predicate.test(t);
            }
        });
    }

    F(Supplier<R> supplier) {
        this(new Function<T, R>() {
            @Override
            public R apply(T t) {
                return supplier.get();
            }
        });
    }
    
    F(IntUnaryOperator intUnaryOperator) {
        this((Function<T, R>) new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer t) {
                return intUnaryOperator.applyAsInt(t);
            }
        });
    }
    
    F(LongUnaryOperator longUnaryOperator) {
        this((Function<T, R>) new Function<Long, Long>() {
            @Override
            public Long apply(Long t) {
                return longUnaryOperator.applyAsLong(t);
            }
        });
    }
    
    F(DoubleUnaryOperator doubleUnaryOperator) {
        this((Function<T, R>) new Function<Double, Double>() {
            @Override
            public Double apply(Double t) {
                return doubleUnaryOperator.applyAsDouble(t);
            }
        });
    }
    
    F(IntFunction<R> intFunction) {
        this((Function<T, R>) new Function<Integer, R>() {
            @Override
            public R apply(Integer t) {
                return intFunction.apply(t);
            }
        });
    }
    
    F(LongFunction<R> longFunction) {
        this((Function<T, R>) new Function<Long, R>() {
            @Override
            public R apply(Long t) {
                return longFunction.apply(t);
            }
        });
    }
    
    F(DoubleFunction<R> doubleFunction) {
        this((Function<T, R>) new Function<Double, R>() {
            @Override
            public R apply(Double t) {
                return doubleFunction.apply(t);
            }
        });
    }
    
    F(ToIntFunction<T> toIntFunction) {
        this((Function<T, R>) new Function<T, Integer>() {
            @Override
            public Integer apply(T t) {
                return toIntFunction.applyAsInt(t);
            }
        });
    }
    
    F(ToLongFunction<T> toLongFunction) {
        this((Function<T, R>) new Function<T, Long>() {
            @Override
            public Long apply(T t) {
                return toLongFunction.applyAsLong(t);
            }
        });
    }
    
    F(ToDoubleFunction<T> toDoubleFunction) {
        this((Function<T, R>) new Function<T, Double>() {
            @Override
            public Double apply(T t) {
                return toDoubleFunction.applyAsDouble(t);
            }
        });
    }
    
    F(IntToLongFunction intToLongFunction) {
        this((Function<T, R>) new Function<Integer, Long>() {
            @Override
            public Long apply(Integer t) {
                return intToLongFunction.applyAsLong(t);
            }
        });
    }
    
    F(IntToDoubleFunction intToDoubleFunction) {
        this((Function<T, R>) new Function<Integer, Double>() {
            @Override
            public Double apply(Integer t) {
                return intToDoubleFunction.applyAsDouble(t);
            }
        });
    }
    
    F(LongToIntFunction longToIntFunction) {
        this((Function<T, R>) new Function<Long, Integer>() {
            @Override
            public Integer apply(Long t) {
                return longToIntFunction.applyAsInt(t);
            }
        });
    }
    
    F(LongToDoubleFunction longToDoubleFunction) {
        this((Function<T, R>) new Function<Long, Double>() {
            @Override
            public Double apply(Long t) {
                return longToDoubleFunction.applyAsDouble(t);
            }
        });
    }
    
    F(DoubleToIntFunction doubleToIntFunction) {
        this((Function<T, R>) new Function<Double, Integer>() {
            @Override
            public Integer apply(Double t) {
                return doubleToIntFunction.applyAsInt(t);
            }
        });
    }
    
    F(DoubleToLongFunction doubleToLongFunction) {
        this((Function<T, R>) new Function<Double, Long>() {
            @Override
            public Long apply(Double t) {
                return doubleToLongFunction.applyAsLong(t);
            }
        });
    }
    
    F(IntPredicate intPredicate) {
        this(new Function<T, R>() {
            @Override
            public R apply(T t) {
                return (R) (Boolean)intPredicate.test((Integer)t);
            }
        });
    }
    
    F(LongPredicate longPredicate) {
        this((Predicate<T>) new Predicate<Long>() {
            @Override
            public boolean test(Long t) {
                return longPredicate.test(t);
            }
        });
    }
    
    F(DoublePredicate doublePredicate) {
        this((Predicate<T>) new Predicate<Double>() {
            @Override
            public boolean test(Double t) {
                return doublePredicate.test(t);
            }
        });
    }
    
    F(IntConsumer intConsumer) {
        this((Consumer<T>) new Consumer<Integer>() {
            @Override
            public void accept(Integer t) {
                intConsumer.accept(t);
            }
        });
    }
    
    F(LongConsumer longConsumer) {
        this((Consumer<T>) new Consumer<Long>() {
            @Override
            public void accept(Long t) {
                longConsumer.accept(t);
            }
        });
    }
    
    F(DoubleConsumer doubleConsumer) {
        this((Consumer<T>) new Consumer<Double>() {
            @Override
            public void accept(Double t) {
                doubleConsumer.accept(t);
            }
        });
    }
    
    F(IntSupplier intSupplier) {
        this((Supplier<R>) new Supplier<Integer>() {
            @Override
            public Integer get() {
                return intSupplier.getAsInt();
            }
        });
    }
    
    F(LongSupplier longSupplier) {
        this((Supplier<R>) new Supplier<Long>() {
            @Override
            public Long get() {
                return longSupplier.getAsLong();
            }
        });
    }
    
    F(DoubleSupplier doubleSupplier) {
        this((Supplier<R>) new Supplier<Double>() {
            @Override
            public Double get() {
                return doubleSupplier.getAsDouble();
            }
        });
    }
    
    F(BooleanSupplier booleanSupplier) {
        this((Supplier<R>) new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return booleanSupplier.getAsBoolean();
            }
        });
    }
    
    F(BiFunction<T1, T2, R> biFunction) {
        this.biFunction = biFunction;
        this.f = null;
        this.f2 = (T1 t, T2 u) -> biFunction.apply(t, u);
    }

    F(BiConsumer<T1, T2> biConsumer) {
        this(new BiFunction<T1, T2, R>() {
            @Override
            public R apply(T1 t, T2 u) {
                biConsumer.accept((T1)t, (T2)u);
                return null;
            }
        });
    }
    
    F(BiPredicate<T1, T2> biPredicate) {
        this(new BiFunction<T1, T2, R>() {
            @Override
            public R apply(T1 t, T2 u) {
                return (R)(Boolean)biPredicate.test((T1)t, (T2)u);
            }
        });
    }
    
    F(IntBinaryOperator intBinaryOperator) {
        this((BiFunction<T1, T2, R>) new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer t, Integer u) {
                return intBinaryOperator.applyAsInt(t, u);
            }
        });
    }
    
    F(LongBinaryOperator longBinaryOperator) {
        this((BiFunction<T1, T2, R>) new BiFunction<Long, Long, Long>() {
            @Override
            public Long apply(Long t, Long u) {
                return longBinaryOperator.applyAsLong(t, u);
            }
        });
    }
    
    F(DoubleBinaryOperator doubleBinaryOperator) {
        this((BiFunction<T1, T2, R>) new BiFunction<Double, Double, Double>() {
            @Override
            public Double apply(Double t, Double u) {
                return doubleBinaryOperator.applyAsDouble(t, u);
            }
        });
    }
    
    F(IntBiFunction<R> intBiFunction) {
        this((BiFunction<T1, T2, R>) new BiFunction<Integer, Integer, R>() {
            @Override
            public R apply(Integer t, Integer u) {
                return intBiFunction.apply(t, u);
            }
        });
    }
    
    F(LongBiFunction<R> longBiFunction) {
        this((BiFunction<T1, T2, R>) new BiFunction<Long, Long, R>() {
            @Override
            public R apply(Long t, Long u) {
                return longBiFunction.apply(t, u);
            }
        });
    }
    
    F(DoubleBiFunction<R> doubleBiFunction) {
        this((BiFunction<T1, T2, R>) new BiFunction<Double, Double, R>() {
            @Override
            public R apply(Double t, Double u) {
                return doubleBiFunction.apply(t, u);
            }
        });
    }
    
    F(ToIntBiFunction<T1, T2> toIntBiFunction) {
        this(new BiFunction<T1, T2, R>() {
            @Override
            public R apply(T1 t, T2 u) {
                return (R)(Integer)toIntBiFunction.applyAsInt(t, u);
            }
        });
    }
    
    F(ToLongBiFunction<T1, T2> toLongBiFunction) {
        this(new BiFunction<T1, T2, R>() {
            @Override
            public R apply(T1 t, T2 u) {
                return (R)(Long)toLongBiFunction.applyAsLong(t, u);
            }
        });
    }
    
    F(ToDoubleBiFunction<T1, T2> toDoubleBiFunction) {
        this(new BiFunction<T1, T2, R>() {
            @Override
            public R apply(T1 t, T2 u) {
                return (R)(Double)toDoubleBiFunction.applyAsDouble(t, u);
            }
        });
    }
    
    F(ObjIntConsumer<T1> objIntConsumer) {
        this((BiFunction<T1, T2, R>) new BiFunction<T1, Integer, R>() {
            @Override
            public R apply(T1 t, Integer u) {
                objIntConsumer.accept(t, u);
                return null;
            }
        });
    }
    
    F(ObjLongConsumer<T1> objLongConsumer) {
        this((BiFunction<T1, T2, R>) new BiFunction<T1, Long, R>() {
            @Override
            public R apply(T1 t, Long u) {
                objLongConsumer.accept(t, u);
                return null;
            }
        });
    }
    
    F(ObjDoubleConsumer<T1> objDoubleConsumer) {
        this((BiFunction<T1, T2, R>) new BiFunction<T1, Double, R>() {
            @Override
            public R apply(T1 t, Double u) {
                objDoubleConsumer.accept(t, u);
                return null;
            }
        });
    }
    
    /**
     * Invokes the wrapped function with <code>null</code>.
     */
    public R call() {
        if (function != null) {
            return function.apply(null);
        }
        throw new IllegalStateException("No appropriate original function is set.");
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
        if (biFunction != null) {
            return biFunction.apply((T1)inputs[0], (T2)inputs[1]);
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }
    
    public FunctionalI<T, R> functional() {
        return f;
    }
    
    public BiFunctionalI<T1, T2, R> biFunctional() {
        return f2;
    }
    
    /**
     * Turns a wrapped {@link BiFunction} <i>f1: (K, V) &rarr; R</i> into a {@link Function} <i>f2: ({@link E}&lt;K, V&gt;) &rarr; R</i>.
     */
    public Function<M.E<T1, T2>, R> eFunction() {
        if (biFunction != null) {
            return (M.E<T1, T2> e) -> 
                biFunction.apply(e.k, e.v);
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }
    
    /**
     * Turns a wrapped {@link BiConsumer} <i>f1: (K, V) &rarr; <code>void</code></i> into a {@link Consumer} <i>f2: ({@link E}&lt;K, V&gt;) &rarr; <code>void</code></i>.
     */
    public Consumer<M.E<T1, T2>> eConsumer() {
        return (M.E<T1, T2> e) -> 
                biFunction.apply(e.k, e.v);
    }
    
    /**
     * Turns a wrapped {@link BiPredicate} <i>f1: (K, V) &rarr; <code>boolean</code></i> into a {@link Predicate} <i>f2: ({@link E}&lt;K, V&gt;) &rarr; <code>boolean</code></i>.
     */
    public Predicate<M.E<T1, T2>> ePredicate() {
        return (M.E<T1, T2> e) -> 
                (Boolean)biFunction.apply(e.k, e.v);
    }
    
    /**
     * Turns a wrapped {@link ToDoubleBiFunction} <i>f1: (K, V) &rarr; <code>double</code></i> into a {@link ToDoubleFunction} <i>f2: ({@link E}&lt;K, V&gt;) &rarr; <code>double</code></i>.
     */
    public ToDoubleFunction<M.E<T1, T2>> eToDoubleFunction() {
        return (M.E<T1, T2> e) -> 
                (Double)biFunction.apply(e.k, e.v);
    }
    
    /**
     * Turns a wrapped {@link Function} <i>f1: ({@link E}&lt;K, V&gt;) &rarr; R</i> into a {@link BiFunction} <i>f2: (K, V) &rarr; R</i>.
     */
    public BiFunction<T1, T2, R> eBiFunction() {
        if (function != null) {
            return (T1 t, T2 u) -> 
                function.apply((T) M.e(t, u));
        }
        throw new IllegalStateException("No appropriate original function is set.");
    }
    
    /**
     * Turns a wrapped {@link Consumer} <i>f1: ({@link E}&lt;K, V&gt;) &rarr; <code>void</code></i> into a {@link BiConsumer} <i>f2: (K, V) &rarr; <code>void</code></i>.
     */
    public BiConsumer<T1, T2> eBiConsumer() {
        return (T1 t, T2 u) -> 
                function.apply((T) M.e(t, u));
    }
    
    /**
     * Turns a wrapped {@link Predicate} <i>f1: ({@link E}&lt;K, V&gt;) &rarr; <code>boolean</code></i> into a {@link BiPredicate} <i>f2: (K, V) &rarr; <code>boolean</code></i>.
     */
    public BiPredicate<T1, T2> eBiPredicate() {
        return (T1 t, T2 u) -> 
                (Boolean)function.apply((T) M.e(t, u));
    }

    /**
     * Like {@link ReadonlyIndexedI#get(Object)}, but invokes {@link #call(Object)} with the <code>key</code> provided.
     */
    @Override
    public <VN extends R> VN get(T key) {
        VN ret = (VN) call(key);
        if (ret == null) {
            return (VN) defaultFunction.apply(key);
        }
        return ret;
    }

    /**
     * Like {@link ReadonlyIndexedI#get(Object)}, but invokes {@link #call(Object)} with the <code>key</code> provided.
     */
    @Override
    public <VN extends R> VN getOrDefault(T key, VN defaultValue) {
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
        this.defaultFunction = defaultValue;
        return this;
    }
    
    /**
     * Creates a {@link F} which wraps the function provided.
     */
    public static <T, R> F<T, T, T, R> f(Function<T, R> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static <T, R> F<T, T, T, R> function(Function<T, R> function) {
        return f(function);
    }
    
    /**
     * Shorthand for {@link #f(Function)} followed by {@link #functional()}.
     */
    public static <T, R> FunctionalI<T, R> F(Function<T, R> function) {
        return f(function).functional();
    }
    
    /**
     * @see #F(Function)
     */
    public static <T, R> FunctionalI<T, R> functional(Function<T, R> function) {
        return F(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static <T> F<T, T, T, Void> f(Consumer<T> function) {
        return new F<>(function);
    }
    
    /**
     * @see #F(Function)
     */
    public static <T> FunctionalI<T, Void> F(Consumer<T> function) {
        return f(function).functional();
    }
    
    /**
     * @see #f(Function)
     */
    public static <T> F<T, T, T, Boolean> f(Predicate<T> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static <R> F<Void, Void, Void, R> f(Supplier<R> function) {
        return new F<>(function);
    }
    
    /**
     * @see #F(Function)
     */
    public static <R> FunctionalI<Void, R> F(Supplier<R> function) {
        return f(function).functional();
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Integer, Integer, Integer, Integer> f(IntUnaryOperator function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Long, Long, Long, Long> f(LongUnaryOperator function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Double, Double, Double, Double> f(DoubleUnaryOperator function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static <R> F<Integer, Integer, Integer, R> f(IntFunction<R> function) {
        return new F<>(function);
    }
    
    /**
     * @see #F(Function)
     */
    public static <R> FunctionalI<Integer, R> F(IntFunction<R> function) {
        return f(function).functional();
    }
    
    /**
     * @see #f(Function)
     */
    public static <R> F<Long, Long, Long, R> f(LongFunction<R> function) {
        return new F<>(function);
    }
    
    /**
     * @see #F(Function)
     */
    public static <R> FunctionalI<Long, R> F(LongFunction<R> function) {
        return f(function).functional();
    }
    
    /**
     * @see #f(Function)
     */
    public static <R> F<Double, Double, Double, R> f(DoubleFunction<R> function) {
        return new F<>(function);
    }
    
    /**
     * @see #F(Function)
     */
    public static <R> FunctionalI<Double, R> F(DoubleFunction<R> function) {
        return f(function).functional();
    }
    
    /**
     * @see #f(Function)
     */
    public static <T> F<T, T, T, Integer> f(ToIntFunction<T> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static <T> F<T, T, T, Long> f(ToLongFunction<T> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static <T> F<T, T, T, Double> f(ToDoubleFunction<T> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Integer, Integer, Integer, Long> f(IntToLongFunction function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Integer, Integer, Integer, Double> f(IntToDoubleFunction function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Long, Long, Long, Integer> f(LongToIntFunction function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Long, Long, Long, Double> f(LongToDoubleFunction function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Double, Double, Double, Integer> f(DoubleToIntFunction function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Double, Double, Double, Long> f(DoubleToLongFunction function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Integer, Integer, Integer, Boolean> f(IntPredicate function) {
        return new F<>(function);
    }
        
    /**
     * @see #f(Function)
     */
    public static F<Long, Long, Long, Boolean> f(LongPredicate function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Double, Double, Double, Boolean> f(DoublePredicate function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Integer, Integer, Integer, Void> f(IntConsumer function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Long, Long, Long, Void> f(LongConsumer function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Double, Double, Double, Void> f(DoubleConsumer function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Void, Void, Void, Integer> f(IntSupplier function) {
        return new F<>(function);
    }
    
    /**
     * @see #F(Function)
     */
    public static <R> FunctionalI<Void, Integer> F(IntSupplier function) {
        return f(function).functional();
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Void, Void, Void, Long> f(LongSupplier function) {
        return new F<>(function);
    }
    
    /**
     * @see #F(Function)
     */
    public static <R> FunctionalI<Void, Long> F(LongSupplier function) {
        return f(function).functional();
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Void, Void, Void, Double> f(DoubleSupplier function) {
        return new F<>(function);
    }
    
    /**
     * @see #F(Function)
     */
    public static <R> FunctionalI<Void, Double> F(DoubleSupplier function) {
        return f(function).functional();
    }
    
    /**
     * @see #f(Function)
     */
    public static F<Void, Void, Void, Boolean> f(BooleanSupplier function) {
        return new F<>(function);
    }
    
    /**
     * @see #F(Function)
     */
    public static <R> FunctionalI<Void, Boolean> F(BooleanSupplier function) {
        return f(function).functional();
    }
    
    /**
     * @see #f(Function)
     */
    public static <T, K extends T, V extends T, R> F<T, K, V, R> f(BiFunction<K, V, R> function) {
        return new F<>(function);
    }
    
    /**
     * Shorthand for {@link #f(BiFunction)} followed by {@link #biFunctional()}.
     */
    public static <T1, T2, R> BiFunctionalI<T1, T2, R> F(BiFunction<T1, T2, R> function) {
        return f(function).biFunctional();
    }
    
    /**
     * @see #F(BiFunction)
     */
    public static <T1, T2, R> BiFunctionalI<T1, T2, R> biFunctional(BiFunction<T1, T2, R> function) {
        return F(function);
    }
    
    /**
     * @see #f(Function)
     */
    public static <T, T1 extends T, T2 extends T> F<T, T1, T2, Void> f(BiConsumer<T1, T2> function) {
        return new F<>(function);
    }
    
    /**
     * @see #F(Function)
     */
    public static <T1, T2> BiFunctionalI<T1, T2, Void> F(BiConsumer<T1, T2> function) {
        return f(function).biFunctional();
    }
    
    /**
     * @see #f(BiFunction)
     */
    public static <T, K extends T, V extends T> F<T, K, V, Boolean> f(BiPredicate<K, V> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(BiFunction)
     */
    public static F<Integer, Integer, Integer, Integer> f(IntBinaryOperator function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(BiFunction)
     */
    public static F<Long, Long, Long, Long> f(LongBinaryOperator function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(BiFunction)
     */
    public static F<Double, Double, Double, Double> f(DoubleBinaryOperator function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(BiFunction)
     */
    public static <R> F<Integer, Integer, Integer, R> f(IntBiFunction<R> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(BiFunction)
     */
    public static <R> F<Long, Long, Long, R> f(LongBiFunction<R> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(BiFunction)
     */
    public static <R> F<Double, Double, Double, R> f(DoubleBiFunction<R> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(BiFunction)
     */
    public static <T, K extends T, V extends T> F<T, K, V, Integer> f(ToIntBiFunction<K, V> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(BiFunction)
     */
    public static <T, K extends T, V extends T> F<T, K, V, Long> f(ToLongBiFunction<K, V> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(BiFunction)
     */
    public static <T, K extends T, V extends T> F<T, K, V, Double> f(ToDoubleBiFunction<K, V> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(BiFunction)
     */
    public static <K> F<Object, K, Integer, Void> f(ObjIntConsumer<K> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(BiFunction)
     */
    public static <K> F<Object, K, Long, Void> f(ObjLongConsumer<K> function) {
        return new F<>(function);
    }
    
    /**
     * @see #f(BiFunction)
     */
    public static <K> F<Object, K, Double, Void> f(ObjDoubleConsumer<K> function) {
        return new F<>(function);
    }
    
    /**
     * @see #eFunction()
     */
    public static <K, V, R> Function<M.E<K, V>, R> function(BiFunction<K, V, R> function) {
      return new F(function).eFunction();
    }
    
    /**
     * @see #eConsumer()
     */
    public static <K, V> Consumer<M.E<K, V>> consumer(BiConsumer<K, V> function) {
        return new F(function).eConsumer();
    }
    
    /**
     * @see #ePredicate()
     */
    public static <K, V> Predicate<M.E<K, V>> predicate(BiPredicate<K, V> function) {
        return new F(function).ePredicate();
    }
    
    /**
     * @see #eToDoubleFunction()
     */
    public static <K, V> ToDoubleFunction<M.E<? extends K, ? extends V>> toDoubleFunction(ToDoubleBiFunction<? extends K, ? extends V> function) {
        return new F(function).eToDoubleFunction();
    }
    
    /**
     * @see #eBiFunction()
     */
    public static <K, V, R> BiFunction<K, V, R> biFunction(Function<E<K, V>, R> function) {
        return new F(function).eBiFunction();
    }
    
    /**
     * @see #eBiConsumer()
     */
    public static <K, V> BiConsumer<K, V> biConsumer(Consumer<E<K, V>> function) {
        return new F(function).eBiConsumer();
    }
    
    /**
     * @see #eBiPredicate()
     */
    public static <K, V> BiPredicate<K, V> biPredicate(Predicate<E<K, V>> function) {
        return new F(function).eBiPredicate();
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

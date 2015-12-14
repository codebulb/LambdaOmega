package ch.codebulb.lambdaomega.abstractions;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;

/**
 * A multi-interface providing a single unified API for all 1-ary {@link FunctionalInterface}s of Java 8 and LambdaOmega.
 * Implement / use {@link #call(Object)} / {@link #call()} to call this function.<p/>
 * 
 * Note: {@link UnaryOperator} is not covered by this interface as it is mutually incompatible with {@link Function}.
 * 
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface FunctionalI<T, R> extends Function<T, R>, Predicate<T>, Consumer<T>, Supplier<R>,
        IntUnaryOperator, LongUnaryOperator, DoubleUnaryOperator,
        IntFunction<R>, LongFunction<R>, DoubleFunction<R>,
        ToIntFunction<T>, ToLongFunction<T>, ToDoubleFunction<T>,
        IntToLongFunction, IntToDoubleFunction, LongToIntFunction, LongToDoubleFunction, DoubleToIntFunction, DoubleToLongFunction,
        IntPredicate, LongPredicate, DoublePredicate,
        IntConsumer, LongConsumer, DoubleConsumer,
        IntSupplier, LongSupplier, DoubleSupplier, BooleanSupplier {
    
    /**
     * Calls this function with the given arguments.
     */
    public R call(T t);
    
    /**
     * Calls this function without arguments / with <code>null</code> as argument.
     */
    public default R call() {
        return call(null);
    }
    
    @Override
    public default R apply(T t) {
        return call(t);
    }
    
    @Override
    public default FunctionalI<T, R> negate() {
        Predicate<T> predicate = t -> this.test(t);
        
        return new FunctionalI<T, R>() {
            @Override
            public R call(T t) {
                return (R)(Boolean)predicate.negate().test(t);
            }
        };
    }

    @Override
    public default boolean test(T t) {
        return (Boolean)call(t);
    }

    @Override
    public default void accept(T t) {
        call(t);
    }

    @Override
    public default R get() {
        return call(null);
    }
    
    @Override
    public default int applyAsInt(int operand) {
        return (Integer)call((T) Integer.valueOf(operand));
    }

    @Override
    public default long applyAsLong(long operand) {
        return (Long)call((T) Long.valueOf(operand));
    }

    @Override
    public default double applyAsDouble(double operand) {
        return (Double)call((T) Double.valueOf(operand));
    }
    
    @Override
    public default R apply(int value) {
        return call((T) Integer.valueOf(value));
    }

    @Override
    public default R apply(long value) {
        return call((T) Long.valueOf(value));
    }

    @Override
    public default R apply(double value) {
        return call((T) Double.valueOf(value));
    }
    
    @Override
    public default int applyAsInt(T value) {
        return (Integer)call(value);
    }

    @Override
    public default long applyAsLong(T value) {
        return (Long)call(value);
    }

    @Override
    public default double applyAsDouble(T value) {
        return (Double)call(value);
    }
    
    @Override
    public default long applyAsLong(int value) {
        return (Long)call((T) Integer.valueOf(value));
    }

    @Override
    public default double applyAsDouble(int value) {
        return (Double)call((T) Integer.valueOf(value));
    }

    @Override
    public default int applyAsInt(long value) {
        return (Integer)call((T) Long.valueOf(value));
    }

    @Override
    public default double applyAsDouble(long value) {
        return (Double)call((T) Long.valueOf(value));
    }

    @Override
    public default int applyAsInt(double value) {
        return (Integer)call((T) Double.valueOf(value));
    }

    @Override
    public default long applyAsLong(double value) {
        return (Long)call((T) Double.valueOf(value));
    }
    
    @Override
    public default boolean test(int value) {
        return (Boolean)call((T) Integer.valueOf(value));
    }

    @Override
    public default boolean test(long value) {
        return (Boolean)call((T) Long.valueOf(value));
    }

    @Override
    public default boolean test(double value) {
        return (Boolean)call((T) Double.valueOf(value));
    }
    
    @Override
    public default void accept(int value) {
        call((T) Integer.valueOf(value));
    }

    @Override
    public default void accept(long value) {
        call((T) Long.valueOf(value));
    }

    @Override
    public default void accept(double value) {
        call((T) Double.valueOf(value));
    }

    @Override
    public default int getAsInt() {
        return (Integer)call(null);
    }

    @Override
    public default long getAsLong() {
        return (Long)call(null);
    }

    @Override
    public default double getAsDouble() {
        return (Double)call(null);
    }

    @Override
    public default boolean getAsBoolean() {
        return (Boolean)call(null);
    }
}

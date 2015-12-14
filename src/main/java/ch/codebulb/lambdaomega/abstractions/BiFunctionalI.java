package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.abstractions.functions.DoubleBiFunction;
import ch.codebulb.lambdaomega.abstractions.functions.IntBiFunction;
import ch.codebulb.lambdaomega.abstractions.functions.LongBiFunction;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

/**
 * A multi-interface providing a single unified API for all 2-ary {@link FunctionalInterface}s of Java 8 and LambdaOmega.
 * Implement / use {@link #call(Object, Object)} / {@link #call()} to call this function.<p/>
 * 
 * Note: {@link BinaryOperator} is not covered by this interface as it is mutually incompatible with {@link BiFunction}.
 * 
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface BiFunctionalI<T, U, R> extends BiFunction<T, U, R>, BiConsumer<T, U>, BiPredicate<T, U>,
        IntBinaryOperator, LongBinaryOperator, DoubleBinaryOperator,
        IntBiFunction<R>, LongBiFunction<R>, DoubleBiFunction<R>,
        ToIntBiFunction<T, U>, ToLongBiFunction<T, U>, ToDoubleBiFunction<T, U>,
        ObjIntConsumer<T>, ObjLongConsumer<T>, ObjDoubleConsumer<T> {
    /**
     * Calls this function with the given arguments.
     */  
    public R call(T t, U u);
    
    /**
     * Calls this function without arguments / with <code>null</code> as argument.
     */
    public default R call() {
        return call(null, null);
    }
    
    @Override
    public default R apply(T t, U u) {
        return call(t, u);
    }

    @Override
    public default void accept(T t, U u) {
        call(t, u);
    }

    @Override
    public default boolean test(T t, U u) {
        return (Boolean)call(t, u);
    }

    @Override
    public default int applyAsInt(int left, int right) {
        return (Integer)call((T)Integer.valueOf(left), (U)Integer.valueOf(right));
    }

    @Override
    public default long applyAsLong(long left, long right) {
        return (Long)call((T)Long.valueOf(left), (U)Long.valueOf(right));
    }

    @Override
    public default double applyAsDouble(double left, double right) {
        return (Double)call((T)Double.valueOf(left), (U)Double.valueOf(right));
    }

    @Override
    public default R apply(int t, int u) {
        return call((T)Integer.valueOf(t), (U)Integer.valueOf(u));
    }

    @Override
    public default R apply(long t, long u) {
        return call((T)Long.valueOf(t), (U)Long.valueOf(u));
    }

    @Override
    public default R apply(double t, double u) {
        return call((T)Double.valueOf(t), (U)Double.valueOf(u));
    }
    
    @Override
    public default int applyAsInt(T t, U u) {
        return (Integer)call(t, u);
    }

    @Override
    public default long applyAsLong(T t, U u) {
        return (Long)call(t, u);
    }

    @Override
    public default double applyAsDouble(T t, U u) {
        return (Double)call(t, u);
    }

    @Override
    public default void accept(T t, int u) {
        call(t, (U) Integer.valueOf(u));
    }

    @Override
    public default void accept(T t, long u) {
        call(t, (U) Long.valueOf(u));
    }

    @Override
    public default void accept(T t, double u) {
        call(t, (U) Double.valueOf(u));
    }
}

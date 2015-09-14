package ch.codebulb.lambdaomega.abstractions;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

@FunctionalInterface
// TODO Build into F and write tests
public interface BiFunctionalI<T, U, R> extends BiFunction<T, U, R>, BiConsumer<T, U>, BiPredicate<T, U>,
        ToIntBiFunction<T, U>, ToLongBiFunction<T, U>, ToDoubleBiFunction<T, U>,
        ObjIntConsumer<T>, ObjLongConsumer<T>, ObjDoubleConsumer<T> {
    public R call(T t, U u);
    
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

package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.F.F;
import static ch.codebulb.lambdaomega.F.f;
import static ch.codebulb.lambdaomega.L.*;
import ch.codebulb.lambdaomega.M.E;
import static ch.codebulb.lambdaomega.M.e;
import static ch.codebulb.lambdaomega.TestUtil.assertEquals;
import ch.codebulb.lambdaomega.abstractions.FunctionalI;
import ch.codebulb.lambdaomega.abstractions.I;
import java.util.List;
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
import java.util.stream.Collectors;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FTest {
    @Test
    public void testCall() {
        // Function
        assertEquals(1, f((Integer it) -> new Integer(it + 1)).call(0));
        // Consumer
        L<Integer> consumerList = l();
        f((Integer it) -> {consumerList.a(it);}).call(1);
        assertEquals(l(1), consumerList);
        // Predicate
        assertTrue(f((Integer it) -> it > 0).call(2));
        // Supplier
        assertEquals(1, f(() -> new Integer(1)).call());
        
        // IntUnaryOperator
        assertEquals(1, f((int it) -> it + 1).call(0));
        // LongUnaryOperator
        assertEquals(1l, f((long it) -> it + 1).call(0l));
        // DoubleUnaryOperator
        assertEquals(1d, f((double it) -> it + 1).call(0d));
        
        // IntFunction
        assertEquals(1, f((int it) -> new Integer(it + 1)).call(0));
        // LongFunction
        assertEquals(1, f((long it) -> new Integer((int)it + 1)).call(0l));
        // DoubleFunction
        assertEquals(1, f((double it) -> new Integer((int)it + 1)).call(0d));
        
        // ToIntFunction
        assertEquals(1, f((String it) -> Integer.parseInt(it)).call("1"));
        // ToLongFunction
        assertEquals(1l, f((String it) -> Long.parseLong(it)).call("1"));
        // ToDoubleFunction
        assertEquals(1d, f((String it) -> Double.parseDouble(it)).call("1"));
        
        // IntToLongFunction
        assertEquals(1l, f((int it) -> (long)it).call(1));
        // IntToDoubleFunction
        assertEquals(1d, f((int it) -> (double)it).call(1));
        // LongToIntFunction
        assertEquals(1, f((long it) -> (int)it).call(1l));
        // LongToDoubleFunction
        assertEquals(1d, f((long it) -> (double)it).call(1l));
        // DoubleToIntFunction
        assertEquals(1, f((double it) -> (int)it).call(1d));
        // DoubleToLongFunction
        assertEquals(1l, f((double it) -> (long)it).call(1d));
        
        // IntPredicate
        assertEquals(true, f((int it) -> true).call(0));
        // LongPredicate
        assertEquals(true, f((long it) -> true).call(0l));
        // DoublePredicate
        assertEquals(true, f((double it) -> true).call(0d));
        
        // IntConsumer
        L<Integer> intConsumerList = l();
        f((int it) -> {intConsumerList.a(it);}).call(1);
        assertEquals(1, intConsumerList.g(0));
        // LongConsumer
        L<Long> longConsumerList = l();
        f((long it) -> {longConsumerList.a(it);}).call(1l);
        assertEquals(1l, longConsumerList.g(0));
        // DoubleConsumer
        L<Double> doubleConsumerList = l();
        f((double it) -> {doubleConsumerList.a(it);}).call(1d);
        assertEquals(1d, doubleConsumerList.g(0));
        
        // IntSupplier
        assertEquals(1, f(() -> 1).call());
        // LongSupplier
        assertEquals(1l, f(() -> 1l).call());
        // DoubleSupplier
        assertEquals(1d, f(() -> 1d).call());
        
        // BiFunction
        assertEquals(3, f((Integer x, Integer y) -> x + y).call(1, 2));
        // BiConsumer
        L<Integer> consumerList2 = l();
        f((Integer x, Integer y) -> {consumerList2.a(x).a(y);}).call(1, 2);
        assertEquals(l(1, 2), consumerList2);
        // BiPredicate
        assertTrue(f((Integer x, Integer y) -> x > 0 && y < 0).call(2, -1));
    }
    
    @Test
    public void testGet() {
        assertEquals(1, f((Integer it) -> it + 1).get(0));
        assertEquals(1, f((Integer it) -> it + 1).g(0));
        assertEquals(list(1, 2, 3), f((Integer it) -> it + 1).get(0, 1, 2));
        assertEquals(l(1, 2, 3), f((Integer it) -> it + 1).Get(0, 1, 2));
        assertEquals(l(1, 2, 3), f((Integer it) -> it + 1).g(0, 1, 2));
        
        assertEquals(list(1, 3, 2), f((Integer it) -> it + 1).getAll(list(0, 2), list(1)));
        assertEquals(list(1, 3, 2), f((Integer it) -> it + 1).GetAll(list(0, 2), list(1)).l);
        assertEquals(list(1, 3, 2), f((Integer it) -> it + 1).G(list(0, 2), list(1)).l);
        
        assertEquals(1, f((Boolean it) -> it ? 1 : null).getOrDefault(true, 2));
        assertEquals(2, f((Boolean it) -> it ? 1 : null).getOrDefault(false, 2));
    }
    
    @Test
    public void testCompare() {
        assertEquals(list(-1, 0, 1, 2), list(2, -1, 1, 0).stream().sorted(F.compare()).collect(Collectors.toList()));
        
        // other comparison tests are implicitly made in L / M tests
    }
    
    @Test
    public void testConvert() {
        assertEquals(1, F.function((Integer k, Integer v) -> k - v).apply(e(3, 2)));
        assertEquals(1, F.biFunction((E<Integer, Integer> it) -> it.k - it.v).apply(3, 2));
        
        L<Integer> consumerList = l();
        F.consumer((Integer k, Integer v) -> consumerList.a(k).a(v)).accept(e(1, 2));
        assertEquals(l(1, 2), consumerList);
        L<Integer> consumerList2 = l();
        F.biConsumer((E<Integer, Integer> it) -> consumerList2.a(it.k).a(it.v)).accept(1, 2);
        assertEquals(l(1, 2), consumerList2);
        
        assertTrue(F.predicate((Integer k, Integer v) -> k > 0 && v < 0).test(e(2, -1)));
        assertTrue(F.biPredicate((E<Integer, Integer> it) -> it.k > 0 && it.v < 0).test(2, -1));
    }
    
    @Test
    public void testWithDefault() {
        I<Boolean, List<Integer>> fWithDefault = f((Boolean it) -> it ? list(0) : null).WithDefault(it -> list(10));
        assertEquals(list(0), fWithDefault.get(true));
        assertEquals(list(10), fWithDefault.get(false));
        
        fWithDefault.get(true).add(9);
        // return a newly created element
        assertEquals(list(0), fWithDefault.get(true));
        fWithDefault.get(false).add(9);
        // return a newly created element
        assertEquals(list(10), fWithDefault.get(false));
    }
    
    @Test
    public void testFunctional() {
        // Function
        Function<Integer, Integer> function = x -> x*2;
        assertEquals(6, function.apply(3));
        FunctionalI<Integer, Integer> functional = x -> x*2;
        assertEquals(6, functional.call(3));
        functional = x -> x*2;
        assertEquals(6, functional.call(3));
        function = F((Integer x) -> x*2);
        assertEquals(6, function.apply(3));
        
        // Consumer
        L<Integer> consumerList = l();
        Consumer<Integer> consumer = x -> consumerList.a(x);
        consumer.accept(6);
        assertEquals(6, consumerList.g(0));
        FunctionalI<Integer, Void> consumerFunctional = F(x -> {consumerList.a(x);});
        consumerFunctional.call(6);
        assertEquals(6, consumerList.g(1));
        consumer = F(x -> {consumerList.a(x);});
        consumer.accept(6);
        assertEquals(6, consumerList.g(2));
        
        // Predicate
        Predicate<Integer> predicate = x -> x < 9;
        assertEquals(true, predicate.test(6));
        FunctionalI<Integer, Boolean> predicateFunctional = f((Integer x) -> x < 9).f;
        assertEquals(true, predicateFunctional.call(6));
        predicateFunctional = x -> x < 9;
        assertEquals(true, predicateFunctional.call(6));
        predicate = F((Integer x) -> x < 9);
        assertEquals(true, predicate.test(6));
        
        // Supplier
        Supplier<Integer> supplier = () -> 6;
        assertEquals(6, supplier.get());
        FunctionalI<Void, Integer> supplierFunctional = F(() -> new Integer(6));
        assertEquals(6, supplierFunctional.call());
        supplier = F(() -> 6);
        assertEquals(6, supplier.get());
        
        // IntUnaryOperator
        IntUnaryOperator intUnaryOperator = (int x) -> x*2;
        assertEquals(6, intUnaryOperator.applyAsInt(3));
        FunctionalI<Integer, Integer> intUnaryOperatorFunctional = F((int x) -> x*2);
        assertEquals(6, intUnaryOperatorFunctional.call(3));
        intUnaryOperator = F((int x) -> x*2);
        assertEquals(6, intUnaryOperator.applyAsInt(3));
        
        // LongUnaryOperator
        LongUnaryOperator longUnaryOperator = (long x) -> x*2;
        assertEquals(6l, longUnaryOperator.applyAsLong(3l));
        FunctionalI<Long, Long> longUnaryOperatorFunctional = f((long x) -> x*2).f;
        assertEquals(6l, longUnaryOperatorFunctional.call(3l));
        longUnaryOperator = F((long x) -> x*2);
        assertEquals(6l, longUnaryOperator.applyAsLong(3l));
        
        // DoubleUnaryOperator
        DoubleUnaryOperator doubleUnaryOperator = (double x) -> x*2;
        assertEquals(6d, doubleUnaryOperator.applyAsDouble(3d));
        FunctionalI<Double, Double> doubleUnaryOperatorFunctional = f((double x) -> x*2).f;
        assertEquals(6d, doubleUnaryOperatorFunctional.call(3d));
        doubleUnaryOperator = F((double x) -> x*2);
        assertEquals(6d, doubleUnaryOperator.applyAsDouble(3d));
        
        // IntFunction
        IntFunction intFunction = (int x) -> x*2;
        assertEquals(6, intFunction.apply(3));
        FunctionalI<Integer, Integer> intFunctional = F((int x) -> new Integer(x*2));
        assertEquals(6, intFunctional.call(3));
        intFunction = F((int x) -> x*2);
        assertEquals(6, intFunction.apply(3));
        
        // LongFunction
        LongFunction longFunction = (long x) -> x*2;
        assertEquals(6l, longFunction.apply(3));
        FunctionalI<Long, Long> longFunctional = F((long x) -> x*2);
        assertEquals(6l, longFunctional.call(3l));
        longFunction = F((long x) -> x*2);
        assertEquals(6l, longFunction.apply(3));
        
        // DoubleFunction
        DoubleFunction doubleFunction = (double x) -> x*2;
        assertEquals(6d, doubleFunction.apply(3));
        FunctionalI<Double, Double> doubleFunctional = F((double x) -> x*2);
        assertEquals(6d, doubleFunctional.call(3d));
        doubleFunction = F((double x) -> x*2);
        assertEquals(6d, doubleFunction.apply(3));
        
        // ToIntFunction
        ToIntFunction<String> toIntFunction = value -> Integer.valueOf(value);
        assertEquals(6, toIntFunction.applyAsInt("6"));
        FunctionalI<String, Integer> toIntFunctional = f((String value) -> Integer.valueOf(value)).f;
        assertEquals(6, toIntFunctional.call("6"));
        toIntFunctional = F((String value) -> Integer.valueOf(value));
        assertEquals(6, toIntFunctional.call("6"));
        toIntFunctional = value -> Integer.valueOf(value);
        assertEquals(6, toIntFunctional.call("6"));
        toIntFunction = F((String value) -> Integer.valueOf(value));
        assertEquals(6, toIntFunction.applyAsInt("6"));
        
        // ToLongFunction
        ToLongFunction<String> toLongFunction = value -> Long.valueOf(value);
        assertEquals(6l, toLongFunction.applyAsLong("6"));
        FunctionalI<String, Long> toLongFunctional = f((String value) -> Long.valueOf(value)).f;
        assertEquals(6l, toLongFunctional.call("6"));
        toLongFunctional = F((String value) -> Long.valueOf(value));
        assertEquals(6l, toLongFunctional.call("6"));
        toLongFunctional = value -> Long.valueOf(value);
        assertEquals(6l, toLongFunctional.call("6"));
        toLongFunction = F((String value) -> Long.valueOf(value));
        assertEquals(6l, toLongFunction.applyAsLong("6"));
        
        // ToDoubleFunction
        ToDoubleFunction<String> toDoubleFunction = value -> Double.valueOf(value);
        assertEquals(6d, toDoubleFunction.applyAsDouble("6"));
        FunctionalI<String, Double> toDoubleFunctional = f((String value) -> Double.valueOf(value)).f;
        assertEquals(6d, toDoubleFunctional.call("6"));
        toDoubleFunctional = F((String value) -> Double.valueOf(value));
        assertEquals(6d, toDoubleFunctional.call("6"));
        toDoubleFunctional = value -> Double.valueOf(value);
        assertEquals(6d, toDoubleFunctional.call("6"));
        toDoubleFunction = F((String value) -> Double.valueOf(value));
        assertEquals(6d, toDoubleFunction.applyAsDouble("6"));
        
        // IntToLongFunction
        IntToLongFunction intToLongFunction = value -> Long.valueOf(value);
        assertEquals(6l, intToLongFunction.applyAsLong(6));
        FunctionalI<Integer, Long> intToLongFunctional = f((int value) -> Long.valueOf(value)).f;
        assertEquals(6l, intToLongFunctional.call(6));
        intToLongFunctional = F((int value) -> Long.valueOf(value));
        assertEquals(6l, intToLongFunctional.call(6));
        intToLongFunctional = value -> Long.valueOf(value);
        assertEquals(6l, intToLongFunctional.call(6));
        intToLongFunction = F((int value) -> Long.valueOf(value));
        assertEquals(6l, intToLongFunction.applyAsLong(6));
        
        // IntToDoubleFunction
        IntToDoubleFunction intToDoubleFunction = value -> Double.valueOf(value);
        assertEquals(6d, intToDoubleFunction.applyAsDouble(6));
        FunctionalI<Integer, Double> intToDoubleFunctional = f((int value) -> Double.valueOf(value)).f;
        assertEquals(6d, intToDoubleFunctional.call(6));
        intToDoubleFunctional = F((int value) -> Double.valueOf(value));
        assertEquals(6d, intToDoubleFunctional.call(6));
        intToDoubleFunctional = value -> Double.valueOf(value);
        assertEquals(6d, intToDoubleFunctional.call(6));
        intToDoubleFunction = F((int value) -> Double.valueOf(value));
        assertEquals(6d, intToDoubleFunction.applyAsDouble(6));
        
        // LongToIntFunction
        LongToIntFunction longToIntFunction = value -> (int)value;
        assertEquals(6, longToIntFunction.applyAsInt(6l));
        FunctionalI<Long, Integer> longToIntFunctional = f((long value) -> (int)value).f;
        assertEquals(6, longToIntFunctional.call(6l));
        longToIntFunctional = F((long value) -> (int)value);
        assertEquals(6, longToIntFunctional.call(6l));
        longToIntFunctional = value -> (int)(long)value;
        assertEquals(6, longToIntFunctional.call(6l));
        longToIntFunction = F((long value) -> (int)value);
        assertEquals(6, longToIntFunction.applyAsInt(6l));
        
        // LongToDoubleFunction
        LongToDoubleFunction longToDoubleFunction = value -> (double)value;
        assertEquals(6d, longToDoubleFunction.applyAsDouble(6l));
        FunctionalI<Long, Double> longToDoubleFunctional = f((long value) -> (double)value).f;
        assertEquals(6d, longToDoubleFunctional.call(6l));
        longToDoubleFunctional = F((long value) -> (double)value);
        assertEquals(6d, longToDoubleFunctional.call(6l));
        longToDoubleFunctional = value -> (double)value;
        assertEquals(6d, longToDoubleFunctional.call(6l));
        longToDoubleFunction = F((long value) -> (double)value);
        assertEquals(6d, longToDoubleFunction.applyAsDouble(6l));
        
        // DoubleToIntFunction
        DoubleToIntFunction doubleToIntFunction = value -> (int)value;
        assertEquals(6, doubleToIntFunction.applyAsInt(6d));
        FunctionalI<Double, Integer> doubleToIntFunctional = f((double value) -> (int)value).f;
        assertEquals(6, doubleToIntFunctional.call(6d));
        doubleToIntFunctional = F((double value) -> (int)value);
        assertEquals(6, doubleToIntFunctional.call(6d));
        doubleToIntFunctional = value -> (int)(double)value;
        assertEquals(6, doubleToIntFunctional.call(6d));
        doubleToIntFunction = F((double value) -> (int)value);
        assertEquals(6, doubleToIntFunction.applyAsInt(6d));
        
        // DoubleToLongFunction
        DoubleToLongFunction doubleToLongFunction = value -> (long)value;
        assertEquals(6l, doubleToLongFunction.applyAsLong(6d));
        FunctionalI<Double, Long> doubleToLongFunctional = f((double value) -> (long)value).f;
        assertEquals(6l, doubleToLongFunctional.call(6d));
        doubleToLongFunctional = F((double value) -> (long)value);
        assertEquals(6l, doubleToLongFunctional.call(6d));
        doubleToLongFunctional = value -> (long)(double)value;
        assertEquals(6l, doubleToLongFunctional.call(6d));
        doubleToLongFunction = F((double value) -> (long)value);
        assertEquals(6l, doubleToLongFunction.applyAsLong(6d));
        
        // IntPredicate
        IntPredicate intPredicate = (int x) -> true;
        assertEquals(true, intPredicate.test(6));
        FunctionalI<Integer, Boolean> intPredicateFunctional = f((int x) -> true).f;
        assertEquals(true, intPredicateFunctional.call(6));
        intPredicate = F((int x) -> true);
        assertEquals(true, intPredicate.test(6));
        
        // LongPredicate
        LongPredicate longPredicate = (long x) -> true;
        assertEquals(true, longPredicate.test(6l));
        FunctionalI<Long, Boolean> longPredicateFunctional = f((long x) -> true).f;
        assertEquals(true, longPredicateFunctional.call(6l));
        longPredicate = F((long x) -> true);
        assertEquals(true, longPredicate.test(6l));
        
        // DoublePredicate
        DoublePredicate doublePredicate = (double x) -> true;
        assertEquals(true, doublePredicate.test(6d));
        FunctionalI<Double, Boolean> doublePredicateFunctional = f((double x) -> true).f;
        assertEquals(true, doublePredicateFunctional.call(6d));
        doublePredicate = F((double x) -> true);
        assertEquals(true, doublePredicate.test(6d));
        
        // IntConsumer
        L<Integer> intConsumerList = l();
        IntConsumer intConsumer = x -> intConsumerList.a(x);
        intConsumer.accept(6);
        assertEquals(6, intConsumerList.g(0));
        FunctionalI<Integer, Void> intConsumerFunctional = f((int x) -> {intConsumerList.a(x);}).f;
        intConsumerFunctional.call(6);
        assertEquals(6, intConsumerList.g(1));
        intConsumer = F((Integer x) -> {intConsumerList.a(x);});
        intConsumer.accept(6);
        assertEquals(6, intConsumerList.g(2));
        
        // LongConsumer
        L<Long> longConsumerList = l();
        LongConsumer longConsumer = x -> longConsumerList.a(x);
        longConsumer.accept(6l);
        assertEquals(6l, longConsumerList.g(0));
        FunctionalI<Long, Void> longConsumerFunctional = f((long x) -> {longConsumerList.a(x);}).f;
        longConsumerFunctional.call(6l);
        assertEquals(6l, longConsumerList.g(1));
        longConsumer = F((Long x) -> {longConsumerList.a(x);});
        longConsumer.accept(6l);
        assertEquals(6l, longConsumerList.g(2));
        
        //  DoubleConsumer
        L<Double> doubleConsumerList = l();
        DoubleConsumer doubleConsumer = x -> doubleConsumerList.a(x);
        doubleConsumer.accept(6d);
        assertEquals(6d, doubleConsumerList.g(0));
        FunctionalI<Double, Void> doubleConsumerFunctional = f((double x) -> {doubleConsumerList.a(x);}).f;
        doubleConsumerFunctional.call(6d);
        assertEquals(6d, doubleConsumerList.g(1));
        doubleConsumer = F((Double x) -> {doubleConsumerList.a(x);});
        doubleConsumer.accept(6d);
        assertEquals(6d, doubleConsumerList.g(2));
        
        // IntSupplier
        IntSupplier intSupplier = () -> 6;
        assertEquals(6, intSupplier.getAsInt());
        FunctionalI<Void, Integer> intSupplierFunctional = F(() -> 6);
        assertEquals(6, intSupplierFunctional.call());
        intSupplier = F(() -> 6);
        assertEquals(6, intSupplier.getAsInt());
        
        // LongSupplier
        LongSupplier LongSupplier = () -> 6l;
        assertEquals(6l, LongSupplier.getAsLong());
        FunctionalI<Void, Long> LongSupplierFunctional = F(() -> 6l);
        assertEquals(6l, LongSupplierFunctional.call());
        LongSupplier = F(() -> 6l);
        assertEquals(6l, LongSupplier.getAsLong());
        
        // DoubleSupplier
        DoubleSupplier DoubleSupplier = () -> 6d;
        assertEquals(6d, DoubleSupplier.getAsDouble());
        FunctionalI<Void, Double> DoubleSupplierFunctional = F(() -> 6d);
        assertEquals(6d, DoubleSupplierFunctional.call());
        DoubleSupplier = F(() -> 6d);
        assertEquals(6d, DoubleSupplier.getAsDouble());
        
        // BooleanSupplier
        BooleanSupplier BooleanSupplier = () -> true;
        assertEquals(true, BooleanSupplier.getAsBoolean());
        FunctionalI<Void, Boolean> BooleanSupplierFunctional = F(() -> true);
        assertEquals(true, BooleanSupplierFunctional.call());
        BooleanSupplier = F(() -> true);
        assertEquals(true, BooleanSupplierFunctional.get());
    }
}
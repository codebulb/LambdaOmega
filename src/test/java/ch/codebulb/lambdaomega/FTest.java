package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.F.f;
import static ch.codebulb.lambdaomega.L.*;
import ch.codebulb.lambdaomega.M.E;
import static ch.codebulb.lambdaomega.M.e;
import static ch.codebulb.lambdaomega.TestUtil.assertEquals;
import ch.codebulb.lambdaomega.abstractions.I;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FTest {
    @Test
    public void testBasics() {
        assertEquals(1, f((Integer it) -> it + 1).call(0));
        assertEquals(3, f((Integer x, Integer y) -> x + y).call(1, 2));
        
        assertEquals(1, f((Integer it) -> it + 1).get(0));
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
        
        L<Integer> consumerList1 = l();
        F.consumer((Integer k, Integer v) -> consumerList1.a(k).a(v)).accept(e(1, 2));
        assertEquals(l(1, 2), consumerList1);
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
}

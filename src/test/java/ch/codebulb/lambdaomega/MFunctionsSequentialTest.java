package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.*;
import static ch.codebulb.lambdaomega.M.*;
import static ch.codebulb.lambdaomega.S.set;
import static ch.codebulb.lambdaomega.TestUtil.*;
import static ch.codebulb.lambdaomega.V2.v;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

import ch.codebulb.lambdaomega.abstractions.SequentialIFunctions;

/**
 * Test cases for the {@link M} implementation for {@link SequentialIFunctions}.
 */
public class MFunctionsSequentialTest {
    private final Set<M.E<String, Integer>> actualSet = set();
    
    @Test
    public void testJoin() {
        try {
            m("a", 0).i("b", 1).i("c", 2).join(", ");
            fail();
        }
        catch (UnsupportedOperationException ex) {}
    }
    
    @Test
    public void testForEach() {
        m("a", 0).i("b", 1).i("c", 2).forEach(it -> actualSet.add(it));
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), actualSet);
    }
    
    @Test
    public void testMapReduce() {
        Set<Integer> explicitSet = set(0, 2, 4);
        Set<Integer> expectedSet = EXPECTED_MAP.entrySet().stream().map(it -> it.getValue() * 2).collect(Collectors.toSet());
        Set<Integer> actualSet = m("a", 0).i("b", 1).i("c", 2).map(it -> it.v * 2);
        assertEquals(explicitSet, expectedSet, actualSet);
        
        int explicitResut = (0 * 2) + (1 * 2) + (2 * 2);
        int expectedResult = EXPECTED_MAP.entrySet().stream().map(it -> it.getValue() * 2).reduce(0, (id, it) -> id + it);
        int actualResult = m("a", 0).i("b", 1).i("c", 2).Map(it -> it.v * 2).reduce(0, (id, it) -> id + it);
        assertEquals(explicitResut, expectedResult, actualResult);
        
        explicitResut = 2;
        expectedResult = m("a", 0).i("b", 1).i("c", 2).i("d", -1).m.entrySet().stream().filter(it -> it.getValue() > 0).collect(Collectors.toList()).size();
        actualResult = m("a", 0).i("b", 1).i("c", 2).i("d", -1).count(it -> it.v > 0);
        assertEquals(explicitResut, expectedResult, actualResult);
        
        explicitResut = 3;
        expectedResult = m("a", 0).i("b", 1).i("c", 2).i("d", -1).m.entrySet().stream().collect(Collectors.summingInt(it -> it.getValue() > 0 ? it.getValue() : 0));
        actualResult = (int) m("a", 0).i("b", 1).i("c", 2).i("d", -1).sum(it -> it.v > 0 ? it.v : 0);
        assertEquals(explicitResut, expectedResult, actualResult);
    }
    
    @Test
    public void testMapEntries() {
        Map<Integer, String> explicitMap = m(0, "a").i(1, "b").i(2, "c").m;
        Map<Integer, String> expectedMap = EXPECTED_MAP.entrySet().stream().collect(Collectors.toMap(it -> it.getValue(), it -> it.getKey()));
        Map<Integer, String> actualMap = m("a", 0).i("b", 1).i("c", 2).mapEntries(it -> e(it.v, it.k));
        Map<Integer, String> actualMapIntermediate = m("a", 0).i("b", 1).i("c", 2).MapEntries(it -> e(it.v, it.k)).m;
        assertEquals(explicitMap, expectedMap, actualMap, actualMapIntermediate);
    }
    
    @Test
    public void testFlatten() {
        try {
            m("a", 0).i("b", 1).i("c", 2).flatten();
            fail();
        }
        catch (UnsupportedOperationException ex) {}
        try {
            m("a", 0).i("b", 1).i("c", 2).Flatten();
            fail();
        }
        catch (UnsupportedOperationException ex) {}
        
        try {
            m("a", 0).i("b", 1).i("c", 2).flattenDeep();
            fail();
        }
        catch (UnsupportedOperationException ex) {}
        try {
            m("a", 0).i("b", 1).i("c", 2).FlattenDeep();
            fail();
        }
        catch (UnsupportedOperationException ex) {}
    }
    
    @Test
    public void testFind() {
        assertTrue(l(e("b", 1), e("c", 2)).contains(m("a", 0).i("b", 1).i("c", 2).i("d", -1).find(it -> it.v > 0)));
        assertNull(m("a", 0).i("b", 1).i("c", 2).find(it -> it.v < 0));
       
        assertEquals(set(e("b", 1), e("c", 2)), m("a", 0).i("b", 1).i("c", 2).i("d", -1).findAll(it -> it.v > 0));
        assertEquals(set(e("b", 1), e("c", 2)), m("a", 0).i("b", 1).i("c", 2).i("d", -1).FindAll(it -> it.v > 0).toSet());
        assertEquals(set(e("b", 1), e("c", 2)), m("a", 0).i("b", 1).i("c", 2).i("d", -1).filter(it -> it.v > 0));
        assertEquals(set(e("b", 1), e("c", 2)), m("a", 0).i("b", 1).i("c", 2).i("d", -1).Filter(it -> it.v > 0).toSet());
        assertEquals(set(e("a", 0), e("d", -1)), m("a", 0).i("b", 1).i("c", 2).i("d", -1).reject(it -> it.v > 0));
        assertEquals(set(e("a", 0), e("d", -1)), m("a", 0).i("b", 1).i("c", 2).i("d", -1).Reject(it -> it.v > 0).toSet());
    }
    
    @Test
    public void testSortBy() {
        assertEquals(list(e("a", 2), e("b", 1), e("c", 0), e("d", -1)), m("a", 2).i("b", 1).i("c", 0).i("d", -1).sortAscBy());
        assertEquals(list(e("d", -1), e("c", 0), e("b", 1), e("a", 2)), m("a", 2).i("b", 1).i("c", 0).i("d", -1).sortDescBy());
        assertEquals(list(e("a", 2), e("b", 1), e("c", 0), e("d", -1)), m("a", 2).i("b", 1).i("c", 0).i("d", -1).SortAscBy().l);
        assertEquals(list(e("d", -1), e("c", 0), e("b", 1), e("a", 2)), m("a", 2).i("b", 1).i("c", 0).i("d", -1).SortDescBy().l);
        
        assertEquals(list(e("d", v(-1, "y")), e("c", v(1, "x")), e("b", v(1, "y")), e("a", v(2, "x"))), 
                m("a", v(2, "x")).i("b", v(1, "y")).i("c", v(1, "x")).i("d", v(-1, "y")).sortAscBy(it -> it.v.get0(), it -> it.v.get1()));
        assertEquals(list(e("d", v(-1, "y")), e("b", v(1, "y")), e("c", v(1, "x")), e("a", v(2, "x"))), 
                m("a", v(2, "x")).i("b", v(1, "y")).i("c", v(1, "x")).i("d", v(-1, "y")).sortBy(it -> v(e -> e.v.get0(), true), it -> v(e -> e.v.get1(), false)));
        assertEquals(list(e("a", v(2, "x")), e("b", v(1, "y")), e("c", v(1, "x")), e("d", v(-1, "y"))), 
                m("a", v(2, "x")).i("b", v(1, "y")).i("c", v(1, "x")).i("d", v(-1, "y")).sortDescBy(it -> it.v.get0(), it -> it.v.get1()));
        
        assertEquals(list(e("d", v(-1, "y")), e("c", v(1, "x")), e("b", v(1, "y")), e("a", v(2, "x"))), 
                m("a", v(2, "x")).i("b", v(1, "y")).i("c", v(1, "x")).i("d", v(-1, "y")).SortAscBy(it -> it.v.get0(), it -> it.v.get1()).l);
        assertEquals(list(e("d", v(-1, "y")), e("b", v(1, "y")), e("c", v(1, "x")), e("a", v(2, "x"))), 
                m("a", v(2, "x")).i("b", v(1, "y")).i("c", v(1, "x")).i("d", v(-1, "y")).SortBy(it -> v(e -> e.v.get0(), true), it -> v(e -> e.v.get1(), false)).l);
        assertEquals(list(e("a", v(2, "x")), e("b", v(1, "y")), e("c", v(1, "x")), e("d", v(-1, "y"))), 
                m("a", v(2, "x")).i("b", v(1, "y")).i("c", v(1, "x")).i("d", v(-1, "y")).SortDescBy(it -> it.v.get0(), it -> it.v.get1()).l);
        
        assertEquals(-1, m("a", 2).i("b", 1).i("c", 0).i("d", -1).min(E::getV).v);
        assertEquals(2, m("a", 2).i("b", 1).i("c", 0).i("d", -1).max(E::getV).v);
    }
    
    @Test
    public void testGroupBy() {
        assertEquals(m().i(true, set(e("a", -1), e("d", -2))).i(false, set(e("b", 0), e("c", 1))).m, 
                m("a", -1).i("b", 0).i("c", 1).i("d", -2).groupBy(it -> it.v < 0));
        assertEquals(m().i(true, set(e("a", -1), e("d", -2))).i(false, set(e("b", 0), e("c", 1))).m, 
                m("a", -1).i("b", 0).i("c", 1).i("d", -2).partition(it -> it.v < 0));
        
        assertEquals(m().i(true, set(e("a", -1), e("d", -2))).i(false, set(e("b", 0), e("c", 1))).m, 
                m("a", -1).i("b", 0).i("c", 1).i("d", -2).GroupBy(it -> it.v < 0).m);
        assertEquals(m().i(true, set(e("a", -1), e("d", -2))).i(false, set(e("b", 0), e("c", 1))).m, 
                m("a", -1).i("b", 0).i("c", 1).i("d", -2).Partition(it -> it.v < 0).m);
    }
    
    @Test
    public void testChecks() {
        assertTrue(m("a", 1).i("b", 2).i("c", 3).every(it -> it.v > 0));
        assertFalse(m("a", 0).i("b", 1).i("c", 2).every(it -> it.v > 0));
        assertTrue(m("a", 0).i("b", 1).i("c", 2).some(it -> it.v > 0));
        assertFalse(m("a", 0).i("b", -1).i("c", -2).some(it -> it.v > 0));
        assertTrue(m("a", 0).i("b", -1).i("c", -2).none(it -> it.v > 0));
        assertFalse(m("a", 0).i("b", -1).i("c", 2).none(it -> it.v > 0));
    }
}

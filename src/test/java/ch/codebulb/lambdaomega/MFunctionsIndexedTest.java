package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.l;
import static ch.codebulb.lambdaomega.M.*;
import static ch.codebulb.lambdaomega.S.set;
import static ch.codebulb.lambdaomega.TestUtil.*;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test cases for the {@link M} implementation for {@link IndexedIFunctions}.
 */
public class MFunctionsIndexedTest {
    private final Map<String, Integer> actual = map(String.class, Integer.class);
    
    @Test
    public void testJoin() {
        // join is not defined for IndexedIFunctions
    }
    
    @Test
    public void testForEach() {
        m("a", 0).i("b", 1).i("c", 2).forEach((k, v) -> actual.put(k, v));
        assertEquals(EXPECTED_MAP, actual);
    }
    
    @Test
    public void testMapReduce() {
        Set<String> explicitSet = set("a0", "b1", "c2");
        Set<String> expectedSet = EXPECTED_MAP.entrySet().stream().map(it -> it.getKey() + it.getValue()).collect(Collectors.toSet());
        Set<String> actualSet = m("a", 0).i("b", 1).i("c", 2).map((k, v) -> k + v);
        assertEquals(explicitSet, expectedSet, actualSet);
        
        int explicitResut = (0 + 0) + (1 + 1) + (2 + 2);
        int expectedResult = m(0, 0).i(1, 1).i(2, 2).m.entrySet().stream().map(it -> it.getKey() + it.getValue()).reduce(0, (id, it) -> id + it);
        int actualResult = m(0, 0).i(1, 1).i(2, 2).Map((k, v) -> k + v).reduce(0, (id, it) -> id + it);
        assertEquals(explicitResut, expectedResult, actualResult);
        
        explicitResut = 2;
        expectedResult = m("a", 0).i("b", 1).i("c", 2).i("d", -1).m.entrySet().stream().filter(it -> it.getValue() > 0).collect(Collectors.toList()).size();
        actualResult = m("a", 0).i("b", 1).i("c", 2).i("d", -1).count((k, v) -> v > 0);
        assertEquals(explicitResut, expectedResult, actualResult);
        
        explicitResut = 3;
        expectedResult = m("a", 0).i("b", 1).i("c", 2).i("d", -1).m.entrySet().stream().collect(Collectors.summingInt(it -> it.getValue() > 0 ? it.getValue() : 0));
        actualResult = (int) m("a", 0).i("b", 1).i("c", 2).i("d", -1).sum((k, v) -> v > 0 ? v : 0);
        assertEquals(explicitResut, expectedResult, actualResult);
    }
    
    @Test
    public void testMapEntries() {
        Map<Integer, String> explicitMap = m(0, "a").i(1, "b").i(2, "c").m;
        Map<Integer, String> expectedMap = EXPECTED_MAP.entrySet().stream().collect(Collectors.toMap(it -> it.getValue(), it -> it.getKey()));
        Map<Integer, String> actualMap = m("a", 0).i("b", 1).i("c", 2).mapEntries((k, v) -> e(v, k));
        Map<Integer, String> actualMapIntermediate = m("a", 0).i("b", 1).i("c", 2).MapEntries((k, v) -> e(v, k)).m;
        assertEquals(explicitMap, expectedMap, actualMap, actualMapIntermediate);
    }
    
    @Test
    public void testFlatten() {
        // flatten is not defined for IndexedIFunctions
    }
    
    @Test
    public void testFind() {
        assertTrue(l(e("b", 1), e("c", 2)).contains(m("a", 0).i("b", 1).i("c", 2).i("d", -1).find((k, v) -> v > 0)));
        assertNull(m("a", 0).i("b", 1).i("c", 2).find((k, v) -> v < 0));
       
        assertEquals(m("b", 1).i("c", 2).m, m("a", 0).i("b", 1).i("c", 2).i("d", -1).findAll((k, v) -> v > 0));
        assertEquals(m("b", 1).i("c", 2), m("a", 0).i("b", 1).i("c", 2).i("d", -1).FindAll((k, v) -> v > 0));
        assertEquals(m("b", 1).i("c", 2).m, m("a", 0).i("b", 1).i("c", 2).i("d", -1).filter((k, v) -> v > 0));
        assertEquals(m("b", 1).i("c", 2), m("a", 0).i("b", 1).i("c", 2).i("d", -1).Filter((k, v) -> v > 0));
        assertEquals(m("a", 0).i("d", -1).m, m("a", 0).i("b", 1).i("c", 2).i("d", -1).reject((k, v) -> v > 0));
        assertEquals(m("a", 0).i("d", -1), m("a", 0).i("b", 1).i("c", 2).i("d", -1).Reject((k, v) -> v > 0));
    }
    
    @Test
    public void testSortBy() {
        // sortBy is not defined for IndexedIFunctions
        
        assertEquals(-1, m("a", 2).i("b", 1).i("c", 0).i("d", -1).min((k, v) -> v).v);
        assertEquals(2, m("a", 2).i("b", 1).i("c", 0).i("d", -1).max((k, v) -> v).v);
    }
    
    @Test
    public void testGroupBy() {
        // groupBy is not defined for IndexedIFunctions
    }
    
    @Test
    public void testChecks() {
        assertTrue(m("a", 1).i("b", 2).i("c", 3).every((k, v) -> v > 0));
        assertFalse(m("a", 0).i("b", 1).i("c", 2).every((k, v) -> v > 0));
        assertTrue(m("a", 0).i("b", 1).i("c", 2).some((k, v) -> v > 0));
        assertFalse(m("a", 0).i("b", -1).i("c", -2).some((k, v) -> v > 0));
        assertTrue(m("a", 0).i("b", -1).i("c", -2).none((k, v) -> v > 0));
        assertFalse(m("a", 0).i("b", -1).i("c", 2).none((k, v) -> v > 0));
    }
}

package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.l;
import static ch.codebulb.lambdaomega.L.list;
import static ch.codebulb.lambdaomega.M.*;
import static ch.codebulb.lambdaomega.S.set;
import static ch.codebulb.lambdaomega.TestUtil.*;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.codebulb.lambdaomega.abstractions.IndexedIFunctions;

/**
 * Test cases for the {@link L} implementation for {@link IndexedIFunctions}.
 */
public class LFunctionsIndexedTest {
    private final Map<String, Integer> actual = map(String.class, Integer.class);
    
    @BeforeClass
    public static void beforeClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = true;
    }
    
    @Test
    public void testJoin() {
        // join is not defined for IndexedIFunctions
    }
    
    @Test
    public void testForEach() {
        l("a", "b", "c").forEach((k, v) -> actual.put(v, k));
        assertEquals(EXPECTED_MAP, actual);
    }
    
    @Test
    public void testMapReduce() {
        List<String> explicitList = list("a0", "b1", "c2");
        List<String> actualList = l("a", "b", "c").map((k, v) -> v + k);
        List<String> actualListParallel = l("a", "b", "c").Parallel().map((k, v) -> v + k);
        assertEquals(explicitList, actualList, actualListParallel);
        
        int explicitResut = (0 + 0) + (1 + 1) + (2 + 2);
        int actualResult = l(0, 1, 2).Map((k, v) -> k + v).reduce(0, (id, it) -> id + it);
        assertEquals(explicitResut, actualResult);
        
        explicitResut = 2;
        actualResult = l(0, 1, 2, -1).count((k, v) -> v > 0);
        assertEquals(explicitResut, actualResult);
        
        explicitResut = 3;
        actualResult = (int) l(0, 1, 2, -1).sum((k, v) -> v > 0 ? v : 0);
        assertEquals(explicitResut, actualResult);
    }
    
    @Test
    public void testMapEntries() {
        Map<String, Integer> explicitMap = m("a", 0).i("b", 1).i("c", 2).m;
        Map<String, Integer> actualMap = l("a", "b", "c").mapEntries((k, v) -> e(v, k));
        Map<String, Integer> actualMapIntermediate = l("a", "b", "c").MapEntries((k, v) -> e(v, k)).m;
        assertEquals(explicitMap, actualMap, actualMapIntermediate);
    }
    
    @Test
    public void testFlatten() {
        // flatten is not defined for IndexedIFunctions
    }
    
    @Test
    public void testFind() {
        assertTrue(l(1, 2).contains(l(0, 1, 2, -1).find((k, v) -> v > 0).v));
        assertNull(l(0, 1, 2).find((k, v) -> v < 0));
       
        assertEquals(m(1, 1).i(2, 2).m, l(0, 1, 2, -1).findAll((k, v) -> v > 0));
        assertEquals(set(1, 2), l(0, 1, 2, -1).FindAll((k, v) -> v > 0).map(M.E::getV));
        assertEquals(m(1, 1).i(2, 2).m, l(0, 1, 2, -1).filter((k, v) -> v > 0));
        assertEquals(set(1, 2), l(0, 1, 2, -1).Filter((k, v) -> v > 0).map(M.E::getV));
        assertEquals(m(0, 0).i(3, -1).m, l(0, 1, 2, -1).reject((k, v) -> v > 0));
        assertEquals(set(0, -1), l(0, 1, 2, -1).Reject((k, v) -> v > 0).map(M.E::getV));
    }
    
    @Test
    public void testSortBy() {
        // sortBy is not defined for IndexedIFunctions
        
        assertEquals(-1, l(0, 1, 2, -1).min((k, v) -> v).v);
        assertEquals(2, l(0, 1, 2, -1).max((k, v) -> v).v);
    }
    
    @Test
    public void testGroupBy() {
        // groupBy is not defined for IndexedIFunctions
    }
    
    @Test
    public void testChecks() {
        assertTrue(l(1, 2, 3).every((k, v) -> v > 0));
        assertFalse(l(0, 1, 2).every((k, v) -> v > 0));
        assertTrue(l(0, 1, 2).some((k, v) -> v > 0));
        assertFalse(l(0, -1, -2).some((k, v) -> v > 0));
        assertTrue(l(0, -1, -2).none((k, v) -> v > 0));
        assertFalse(l(0, -1, 2).none((k, v) -> v > 0));
    }
    
    @AfterClass
    public static void afterClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = false;
    }
}

package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.*;
import static ch.codebulb.lambdaomega.M.*;
import static ch.codebulb.lambdaomega.S.set;
import static ch.codebulb.lambdaomega.TestUtil.*;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

// TODO Complete these tests
public class MFunctionSequentialTest {
    private Map<String, Integer> actual = m().m;
    private Map<String, Integer> expected;
    private Map<String, Integer> explicit;
    private final Set<M.E<String, Integer>> actualSet = set();
    
    @BeforeClass
    public static void beforeClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = true;
    }
    
    @Test
    public void testJoin() {
        assertEquals("0, 1, 2", l(0, 1, 2).join(", "));
    }
    
    @Test
    public void testForEach() {
        m("a", 0).i("b", 1).i("c", 2).forEach(it -> actualSet.add(it));
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), actualSet);
    }
    
    @Test
    public void testMapReduce() {
        Set<Integer>  explicitSet = set(0, 2, 4);
        Set<Integer> expectedSet = EXPECTED_MAP.entrySet().stream().map(it -> it.getValue() * 2).collect(Collectors.toSet());
        Set<Integer> actualSet = C.toSet(m("a", 0).i("b", 1).i("c", 2).map(it -> it.v * 2));
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
    public void testFind() {
        assertTrue(l(e("b", 1), e("c", 2)).contains(m("a", 0).i("b", 1).i("c", 2).i("d", -1).find(it -> it.v > 0)));
        
        assertEquals(set(e("b", 1), e("c", 2)), C.toSet(m("a", 0).i("b", 1).i("c", 2).i("d", -1).findAll(it -> it.v > 0)));
        assertEquals(set(e("b", 1), e("c", 2)), m("a", 0).i("b", 1).i("c", 2).i("d", -1).FindAll(it -> it.v > 0).toSet());
        assertEquals(set(e("a", 0), e("d", -1)), C.toSet(m("a", 0).i("b", 1).i("c", 2).i("d", -1).reject(it -> it.v > 0)));
        assertEquals(set(e("a", 0), e("d", -1)), m("a", 0).i("b", 1).i("c", 2).i("d", -1).Reject(it -> it.v > 0).toSet());
    }

    @AfterClass
    public static void afterClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = false;
    }
}

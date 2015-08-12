package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.*;
import static ch.codebulb.lambdaomega.M.*;
import static ch.codebulb.lambdaomega.TestUtil.*;
import static ch.codebulb.lambdaomega.V2.v;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for the {@link L} implementation for {@link SequentialIFunctions}.
 */
public class LFunctionsSequentialTest {
    private List<Integer> actual = list();
    private List<Integer> expected;
    private List<Integer> explicit;
    
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
        l(0, 1, 2).forEach(it -> actual.add(it));
        assertEquals(EXPECTED_LIST, actual);
    }
    
    @Test
    public void testMapReduce() {
        explicit = l(2, 4, 6).l;
        expected = EXPECTED_LIST.stream().map(it -> it + 1).map(it -> it * 2).collect(Collectors.toList());
        actual = l(0, 1, 2).Map(it -> it + 1).map(it -> it * 2);
        assertEquals(explicit, expected, actual);
        
        int explicitResut = (0 * 2) + (1 * 2) + (2 * 2);
        int expectedResult = EXPECTED_LIST.stream().map(it -> it * 2).reduce(0, (id, it) -> id + it);
        int actualResult = l(0, 1, 2).Map(it -> it * 2).reduce(0, (id, it) -> id + it);
        assertEquals(explicitResut, expectedResult, actualResult);
        
        explicitResut = 2;
        expectedResult = l(0, 1, 2, -1).l.stream().filter(it -> it > 0).collect(Collectors.toList()).size();
        actualResult = l(0, 1, 2).count(it -> it > 0);
        assertEquals(explicitResut, expectedResult, actualResult);
        
        explicitResut = 3;
        expectedResult = l(0, 1, 2, -1).l.stream().collect(Collectors.summingInt(it -> it > 0 ? it : 0));
        actualResult = (int) l(0, 1, 2).sum(it -> it > 0 ? it : 0);
        assertEquals(explicitResut, expectedResult, actualResult);
    }
    
    @Test
    public void testMapEntries() {
        Map<String, Integer> explicitMap = m().i("1", 2).i("2", 4).i("3", 6).m;
        Map<String, Integer> expectedMap = EXPECTED_LIST.stream().map(it -> it + 1).collect(Collectors.toMap(it -> it.toString(), it -> it*2));
        Map<String, Integer> actualMap = l(0, 1, 2).Map(it -> it + 1).mapEntries(it -> e(it.toString(), it*2));
        Map<String, Integer> actualMapIntermediate = l(0, 1, 2).Map(it -> it + 1).MapEntries(it -> e(it.toString(), it*2)).m;
        assertEquals(explicitMap, expectedMap, actualMap, actualMapIntermediate);
    }
    
    @Test
    public void testFlatten() {
        explicit = l(0, 1, 2, 0, 1, 2).l;
        expected = EXPECTED_NESTED_LIST.stream().flatMap(it -> ((List<Integer>)it).stream()).collect(Collectors.toList());
        actual = l(0, l(1, 2), l(0, 1, 2)).flatten();
        List<Integer> actualWithLists = l(0, list(1, 2), list(0, 1, 2)).flatten();
        List<Integer> actualIntermediate = l(0, l(1, 2), l(0, 1, 2)).<Integer> Flatten().l;
        List<Integer> actualIntermediateWithLists = l(0, list(1, 2), list(0, 1, 2)).<Integer> Flatten().l;
        assertEquals(explicit, expected, actual, actualWithLists, actualIntermediate, actualIntermediateWithLists);
        
        explicit = l(0, 1, 2, 0, 1, 2).l;
        Stream<Object> flatMap = l(0, 1, list(2, list(0, 1, 2))).l.stream().flatMap(it -> {
            if (it instanceof L) {
                return ((L<?>)it).stream();
            }
            else if (it instanceof Collection) {
                return ((Collection<?>)it).stream();
            }
            else {
                return Stream.of(it);
            }
        }).flatMap(it -> {
            if (it instanceof L) {
                return ((L<?>)it).stream();
            }
            else if (it instanceof Collection) {
                return ((Collection<?>)it).stream();
            }
            else {
                return Stream.of(it);
            }
        });
        List<Object> expectedTypeless = flatMap.collect(Collectors.toList());
        actual = l(0, 1, l(2, l(0, 1, 2))).<Integer> flattenDeep();
        actualWithLists = l(0, 1, list(2, list(0, 1, 2))).<Integer> flattenDeep();
        actualIntermediate = l(0, 1, l(2, l(0, 1, 2))).<Integer> FlattenDeep().l;
        actualIntermediateWithLists = l(0, 1, list(2, list(0, 1, 2))).<Integer> FlattenDeep().l;
        assertEquals(explicit, expectedTypeless, actual, actualWithLists, actualIntermediate, actualIntermediateWithLists);
    }
    
    @Test
    public void testFind() {
        assertEquals(1, l(0, 1, 2, -1).find(it -> it > 0));
        assertNull(l(0, 1, 2).find(it -> it < 0));
        
        assertEquals(list(1, 2), l(0, 1, 2, -1).findAll(it -> it > 0));
        assertEquals(list(1, 2), l(0, 1, 2, -1).FindAll(it -> it > 0).l);
        assertEquals(list(1, 2), l(0, 1, 2, -1).filter(it -> it > 0));
        assertEquals(list(1, 2), l(0, 1, 2, -1).Filter(it -> it > 0).l);
        assertEquals(list(0, -1), l(0, 1, 2, -1).reject(it -> it > 0));
        assertEquals(list(0, -1), l(0, 1, 2, -1).Reject(it -> it > 0).l);
    }
    
    @Test
    public void testSortBy() {
        assertEquals(list(-1, 0, 1, 2), l(0, -1, 1, 2).sortAscBy());
        assertEquals(list(2, 1, 0, -1), l(0, -1, 1, 2).sortDescBy());
        assertEquals(list(-1, 0, 1, 2), l(0, -1, 1, 2).SortAscBy().l);
        assertEquals(list(2, 1, 0, -1), l(0, -1, 1, 2).SortDescBy().l);
        
        assertEquals(v(2, "x").a(-1, "y").a(0, "y").a(1, "y").l, 
                v(0, "y").a(-1, "y").a(1, "y").a(2, "x").sortAscBy(V2::get1, it -> it.get0()));
        assertEquals(v(1, "x").a(2, "y").a(0, "y").a(-1, "y").l, 
                v(0, "y").a(2, "y").a(-1, "y").a(1, "x").sortBy(it -> v(V2::get1, true), it -> v(V2::get0, false)));
        assertEquals(v(1, "y").a(0, "y").a(-1, "y").a(2, "x").l, 
                v(0, "y").a(-1, "y").a(1, "y").a(2, "x").sortDescBy(V2::get1, it -> it.get0()));
        
        assertEquals(v(2, "x").a(-1, "y").a(0, "y").a(1, "y").l, 
                v(0, "y").a(-1, "y").a(1, "y").a(2, "x").SortAscBy(V2::get1, it -> it.get0()).l);
        assertEquals(v(1, "x").a(2, "y").a(0, "y").a(-1, "y").l, 
                v(0, "y").a(2, "y").a(-1, "y").a(1, "x").SortBy(it -> v(V2::get1, true), it -> v(V2::get0, false)).l);
        assertEquals(v(1, "y").a(0, "y").a(-1, "y").a(2, "x").l, 
                v(0, "y").a(-1, "y").a(1, "y").a(2, "x").SortDescBy(V2::get1, it -> it.get0()).l);
        
        assertEquals(-1, l(0, -1, 1, 2).min(it -> it));
        assertEquals(2, l(0, -1, 1, 2).max(it -> it));
    }
    
    @Test
    public void testGroupBy() {
        assertEquals(m().i(true, list(-1, -2)).i(false, list(0, 1)).m, l(-1, 0, 1, -2).groupBy(it -> it < 0));
        assertEquals(m().i(true, list(-1, -2)).i(false, list(0, 1)).m, l(-1, 0, 1, -2).partition(it -> it < 0));
        
        assertEquals(m().i(true, list(-1, -2)).i(false, list(0, 1)).m, l(-1, 0, 1, -2).GroupBy(it -> it < 0).m);
        assertEquals(m().i(true, list(-1, -2)).i(false, list(0, 1)).m, l(-1, 0, 1, -2).Partition(it -> it < 0).m);
    }
        
    @Test
    public void testChecks() {
        assertTrue(l(1, 2, 3).every(it -> it > 0));
        assertFalse(l(0, 1, 2).every(it -> it > 0));
        assertTrue(l(0, 1, 2).some(it -> it > 0));
        assertFalse(l(0, -1, -2).some(it -> it > 0));
        assertTrue(l(0, -1, -2).none(it -> it > 0));
        assertFalse(l(0, -1, 2).none(it -> it > 0));
    }
    
    @AfterClass
    public static void afterClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = false;
    }
}

package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.*;
import static ch.codebulb.lambdaomega.S.*;
import static ch.codebulb.lambdaomega.M.*;
import static ch.codebulb.lambdaomega.TestUtil.*;
import static ch.codebulb.lambdaomega.V2.v;
import ch.codebulb.lambdaomega.abstractions.SequentialIFunctions;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test cases for the {@link S} implementation for {@link SequentialIFunctions}.
 */
public class SFunctionsSequentialTest {
    private Set<Integer> actual = set();
    private Set<Integer> expected;
    private Set<Integer> explicit;
    
    @Test
    public void testJoin() {
        assertEquals("0, 1, 2", s(0, 1, 2).join(", "));
    }
    
    @Test
    public void testForEach() {
        s(0, 1, 2).forEach(it -> actual.add(it));
        assertEquals(EXPECTED_SET, actual);
    }
    
    @Test
    public void testMapReduce() {
        explicit = s(2, 4, 6).s;
        expected = EXPECTED_SET.stream().map(it -> it + 1).map(it -> it * 2).collect(Collectors.toSet());
        actual = s(0, 1, 2).Map(it -> it + 1).map(it -> it * 2);
        assertEquals(explicit, expected, actual);
        
        int explicitResut = (0 * 2) + (1 * 2) + (2 * 2);
        int expectedResult = EXPECTED_LIST.stream().map(it -> it * 2).reduce(0, (id, it) -> id + it);
        int actualResult = s(0, 1, 2).Map(it -> it * 2).reduce(0, (id, it) -> id + it);
        assertEquals(explicitResut, expectedResult, actualResult);
        
        explicitResut = 2;
        expectedResult = s(0, 1, 2, -1).s.stream().filter(it -> it > 0).collect(Collectors.toList()).size();
        actualResult = s(0, 1, 2).count(it -> it > 0);
        assertEquals(explicitResut, expectedResult, actualResult);
        
        explicitResut = 3;
        expectedResult = s(0, 1, 2, -1).s.stream().collect(Collectors.summingInt(it -> it > 0 ? it : 0));
        actualResult = (int) s(0, 1, 2).sum(it -> it > 0 ? it : 0);
        assertEquals(explicitResut, expectedResult, actualResult);
    }
    
    @Test
    public void testMapEntries() {
        Map<String, Integer> explicitMap = m().i("1", 2).i("2", 4).i("3", 6).m;
        Map<String, Integer> expectedMap = EXPECTED_LIST.stream().map(it -> it + 1).collect(Collectors.toMap(it -> it.toString(), it -> it*2));
        Map<String, Integer> actualMap = s(0, 1, 2).Map(it -> it + 1).mapEntries(it -> e(it.toString(), it*2));
        Map<String, Integer> actualMapIntermediate = s(0, 1, 2).Map(it -> it + 1).MapEntries(it -> e(it.toString(), it*2)).m;
        assertEquals(explicitMap, expectedMap, actualMap, actualMapIntermediate);
    }
    
    @Test
    public void testFlatten() {
        explicit = s(0, 1, 2, 3, 4, 5).s;
        expected = EXPECTED_NESTED_SET.stream().flatMap(it -> ((Set<Integer>)it).stream()).collect(Collectors.toSet());
        actual = s(0, s(1, 2), s(3, 4, 5)).flatten();
        Set<Integer> actualWithSets = s(0, set(1, 2), set(3, 4, 5)).flatten();
        Set<Integer> actualIntermediate = s(0, s(1, 2), s(3, 4, 5)).<Integer> Flatten().s;
        Set<Integer> actualIntermediateWithSets = s(0, set(1, 2), set(3, 4, 5)).<Integer> Flatten().s;
        assertEquals(explicit, expected, actual, actualWithSets, actualIntermediate, actualIntermediateWithSets);
        
        explicit = s(0, 1, 2, 3, 4, 5).s;
        Stream<Object> flatMap = s(0, 1, set(2, set(3, 4, 5))).s.stream().flatMap(it -> {
            if (it instanceof S) {
                return ((S<?>)it).stream();
            }
            else if (it instanceof Collection) {
                return ((Collection<?>)it).stream();
            }
            else {
                return Stream.of(it);
            }
        }).flatMap(it -> {
            if (it instanceof S) {
                return ((S<?>)it).stream();
            }
            else if (it instanceof Collection) {
                return ((Collection<?>)it).stream();
            }
            else {
                return Stream.of(it);
            }
        });
        Set<Object> expectedTypeless = flatMap.collect(Collectors.toSet());
        actual = s(0, 1, s(2, s(3, 4, 5))).<Integer> flattenDeep();
        actualWithSets = s(0, 1, set(2, set(3, 4, 5))).<Integer> flattenDeep();
        actualIntermediate = s(0, 1, s(2, s(3, 4, 5))).<Integer> FlattenDeep().s;
        actualIntermediateWithSets = s(0, 1, set(2, set(3, 4, 5))).<Integer> FlattenDeep().s;
        assertEquals(explicit, expectedTypeless, actual, actualWithSets, actualIntermediate, actualIntermediateWithSets);
    }
    
    @Test
    public void testFind() {
        assertEquals(1, s(0, 1, 2, -1).find(it -> it > 0));
        assertNull(s(0, 1, 2).find(it -> it < 0));
        
        assertEquals(set(1, 2), s(0, 1, 2, -1).findAll(it -> it > 0));
        assertEquals(set(1, 2), s(0, 1, 2, -1).FindAll(it -> it > 0).s);
        assertEquals(set(1, 2), s(0, 1, 2, -1).filter(it -> it > 0));
        assertEquals(set(1, 2), s(0, 1, 2, -1).Filter(it -> it > 0).s);
        assertEquals(set(0, -1), s(0, 1, 2, -1).reject(it -> it > 0));
        assertEquals(set(0, -1), s(0, 1, 2, -1).Reject(it -> it > 0).s);
    }
    
    @Test
    public void testSortBy() {
        assertEquals(list(-1, 0, 1, 2), s(0, -1, 1, 2).sortAscBy());
        assertEquals(list(2, 1, 0, -1), s(0, -1, 1, 2).sortDescBy());
        assertEquals(list(-1, 0, 1, 2), s(0, -1, 1, 2).SortAscBy().l);
        assertEquals(list(2, 1, 0, -1), s(0, -1, 1, 2).SortDescBy().l);
        
        assertEquals(v(2, "x").a(-1, "y").a(0, "y").a(1, "y").l, 
                S(v(0, "y").a(-1, "y").a(1, "y").a(2, "x").l).sortAscBy(V2::get1, it -> it.get0()));
        assertEquals(v(1, "x").a(2, "y").a(0, "y").a(-1, "y").l, 
                S(v(0, "y").a(2, "y").a(-1, "y").a(1, "x").l).sortBy(it -> v(V2::get1, true), it -> v(V2::get0, false)));
        assertEquals(v(1, "y").a(0, "y").a(-1, "y").a(2, "x").l, 
                S(v(0, "y").a(-1, "y").a(1, "y").a(2, "x").l).sortDescBy(V2::get1, it -> it.get0()));
        
        assertEquals(v(2, "x").a(-1, "y").a(0, "y").a(1, "y").l, 
                S(v(0, "y").a(-1, "y").a(1, "y").a(2, "x").l).SortAscBy(V2::get1, it -> it.get0()).l);
        assertEquals(v(1, "x").a(2, "y").a(0, "y").a(-1, "y").l, 
                S(v(0, "y").a(2, "y").a(-1, "y").a(1, "x").l).SortBy(it -> v(V2::get1, true), it -> v(V2::get0, false)).l);
        assertEquals(v(1, "y").a(0, "y").a(-1, "y").a(2, "x").l, 
                S(v(0, "y").a(-1, "y").a(1, "y").a(2, "x").l).SortDescBy(V2::get1, it -> it.get0()).l);
        
        assertEquals(-1, s(0, -1, 1, 2).min(it -> it));
        assertEquals(2, s(0, -1, 1, 2).max(it -> it));
        
        S<Integer> shuffleSet = s(0, 1, 2);
        List<Integer> shuffledSet = shuffleSet.shuffle();
        assertEquals(set(0, 1, 2), shuffleSet.s);
        assertEquals(3, shuffledSet.size());
        assertTrue(shuffledSet.contains(0));
        assertTrue(shuffledSet.contains(1));
        assertTrue(shuffledSet.contains(2));
    }
    
    @Test
    public void testGroupBy() {
        assertEquals(m().i(true, set(-1, -2)).i(false, set(0, 1)).m, s(-1, 0, 1, -2).groupBy(it -> it < 0));
        assertEquals(m().i(true, set(-1, -2)).i(false, set(0, 1)).m, s(-1, 0, 1, -2).partition(it -> it < 0));
        
        assertEquals(m().i(true, set(-1, -2)).i(false, set(0, 1)).m, s(-1, 0, 1, -2).GroupBy(it -> it < 0).m);
        assertEquals(m().i(true, set(-1, -2)).i(false, set(0, 1)).m, s(-1, 0, 1, -2).Partition(it -> it < 0).m);
    }
        
    @Test
    public void testChecks() {
        assertTrue(s(1, 2, 3).every(it -> it > 0));
        assertFalse(s(0, 1, 2).every(it -> it > 0));
        assertTrue(s(0, 1, 2).some(it -> it > 0));
        assertFalse(s(0, -1, -2).some(it -> it > 0));
        assertTrue(s(0, -1, -2).none(it -> it > 0));
        assertFalse(s(0, -1, 2).none(it -> it > 0));
    }
}

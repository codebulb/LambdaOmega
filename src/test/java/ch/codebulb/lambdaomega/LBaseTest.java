package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.L;
import static ch.codebulb.lambdaomega.L.l;
import static ch.codebulb.lambdaomega.L.list;
import static ch.codebulb.lambdaomega.TestUtil.EXPECTED_LIST;
import static ch.codebulb.lambdaomega.TestUtil.EXPECTED_NESTED_LIST;
import static ch.codebulb.lambdaomega.TestUtil.assertEquals;
import ch.codebulb.lambdaomega.abstractions.I;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Basic test cases for {@link L}.
 */
public class LBaseTest {
    @BeforeClass
    public static void beforeClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = true;
    }
    
    @Test
    public void testObject() {
        assertTrue(l().equals(l()));
        assertTrue(l(0, 1, 2).equals(l(0, 1, 2)));
        assertFalse(l(9, 1, 2).equals(l(0, 1, 2)));
        assertTrue(list(0, 1, 2).equals(l(0, 1, 2).l));
        
        assertEquals("L[0, 1, 2]", "L" + EXPECTED_LIST.toString(), l(0, 1, 2).toString());
    }
    
    @Test
    public void testStream() {
        assertFalse(l().stream().isParallel());
        assertFalse(l().Seq().stream().isParallel());
        assertTrue(l().Par().stream().isParallel());
    }
        
    @Test
    public void testWithDefault() {
        I<Integer, List<Integer>> listWithDefault = l(list(5), list(6), list(7)).WithDefault(it -> list(it * 2));
        assertEquals(list(5), listWithDefault.get(0));
        assertEquals(list(10), listWithDefault.get(5));
        
        listWithDefault.get(0).add(9);
        // keep previously created element
        assertEquals(list(5, 9), listWithDefault.get(0));
        listWithDefault.get(5).add(9);
        // return a newly created element
        assertEquals(list(10), listWithDefault.get(5));
    }
    
    @Test
    public void testLiteralConstruction() {
        assertEquals(L.class, l().getClass());
        assertEquals(new ArrayList<Integer>(), l().l);
        assertEquals(new ArrayList<Integer>(), l().toList());
        assertEquals(new ArrayList<Integer>(), l().to(ArrayList.class));
        assertEquals(new ArrayList<Integer>(), list());
        
        assertEquals(EXPECTED_LIST, l(0, 1, 2).l);
        assertEquals(EXPECTED_LIST, list(0, 1, 2));
        
        assertEquals(EXPECTED_LIST, L(EXPECTED_LIST).l);
        assertEquals(EXPECTED_LIST, l(new Integer[]{0, 1, 2}).l);
        
        assertEquals(EXPECTED_LIST, l(Integer.class).a(0, 1, 2).l);
        
        assertEquals(EXPECTED_NESTED_LIST, l(l(0, 1, 2).l, l(0, 1, 2).l).l);
        assertEquals(EXPECTED_NESTED_LIST, L(l(0, 1, 2), l(0, 1, 2)).l);
        
        assertEquals(EXPECTED_LIST, l(3, it -> it).l);
        assertEquals(EXPECTED_LIST, list(3, it -> it));
    }
    
    @AfterClass
    public static void afterClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = false;
    }
}

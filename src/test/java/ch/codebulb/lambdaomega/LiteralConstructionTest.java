package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.*;
import static ch.codebulb.lambdaomega.M.*;
import static ch.codebulb.lambdaomega.TestUtil.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class LiteralConstructionTest {
    @BeforeClass
    public static void beforeClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = true;
    }
    
    @Test
    public void testL() {
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
    }
    
    @Test
    public void testM() {
        assertEquals(M.class, m().getClass());
        assertEquals(new LinkedHashMap<String, Integer>(), m(String.class, Integer.class).m);
        assertEquals(new LinkedHashMap<String, Integer>(), m(Integer.class).m);
        assertEquals(new LinkedHashMap<String, Integer>(), m(String.class, Integer.class).toMap());
        assertEquals(new LinkedHashMap<String, Integer>(), map(String.class, Integer.class));        
        
        assertEquals(EXPECTED_MAP, m().i("a", 0).i("b", 1).i("c", 2).m);
        assertEquals(EXPECTED_MAP, m("a", 0).i("b", 1).i("c", 2).m);
        assertEquals(EXPECTED_MAP, m(e("a", 0), e("b", 1), e("c", 2)).m);
        assertEquals(EXPECTED_MAP, map(e("a", 0), e("b", 1), e("c", 2)));
        
        assertEquals(EXPECTED_MAP, m(String.class, Integer.class).i("a", 0).i("b", 1).i("c", 2).m);
        assertEquals(EXPECTED_MAP, m(Integer.class).i("a", 0).i("b", 1).i("c", 2).m);
    }
    
    @AfterClass
    public static void afterClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = false;
    }
}

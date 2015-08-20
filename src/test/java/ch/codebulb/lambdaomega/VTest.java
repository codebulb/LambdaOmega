package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.list;
import static ch.codebulb.lambdaomega.TestUtil.*;
import static ch.codebulb.lambdaomega.V2.v;
import ch.codebulb.lambdaomega.abstractions.I;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class VTest {
    @Test
    public void testBasics() {
        assertTrue(v(0, "a").equals(v(0, "a")));
        assertFalse(v(1, "a").equals(v(0, "a")));
        assertFalse(v(0, "b").equals(v(0, "a")));
        
        assertEquals("(0, a)", v(0, "a").toString());
    }
    
    @Test
    public void testConvert() {
        assertEquals(list(v(0, "a"), v(1, "b"), v(1, "z")), v(0, "a").a(1, "b").a(1, "z").l);
        assertEquals(list(v(0, "a"), v(1, "b"), v(1, "z")), v(0, "a").a(1, "b").Add(1, "z").l);
    }
    
    @Test
    public void testGet() {
        assertEquals(0, v(0, "a").get0());
        int x;
        x = v(0, "a").get(0);
        assertEquals(0, x);
        assertEquals("a", v(0, "a").get1());
        String y = v(0, "a").get(1);
        assertEquals("a", y);
        
        try {
            assertEquals(0, v(0, "a").get(2));
            fail();
        }
        catch (IllegalArgumentException ex) {}
        
        assertEquals(0, v(0, "a").getOrDefault(0, "b"));
        assertEquals("a", v(0, "a").getOrDefault(1, "b"));
        assertEquals("b", v(0, "a").getOrDefault(2, "b"));
    }
    
    @Test
    public void testSet() {
        assertEquals(v(0, "a"), v(1, "a").set0(0));
        assertEquals(v(0, "a"), v(1, "a").set(0, 0));
        
        assertEquals(v(0, "a"), v(0, "b").set1("a"));
        assertEquals(v(0, "a"), v(0, "b").set(1, "a"));
    }
    
    @Test
    public void testWithDefault() {
        I<Integer, Object> v2WithDefault = v(list(5), list(6)).WithDefault(it -> list(it * 2));
        assertEquals(list(5), (List<Integer>)v2WithDefault.get(0));
        assertEquals(list(10), (List<Integer>)v2WithDefault.get(5));
        
        ((List<Integer>)v2WithDefault.get(0)).add(9);
        // keep previously created element
        assertEquals(list(5, 9), (List<Integer>)v2WithDefault.get(0));
        ((List<Integer>)v2WithDefault.get(5)).add(9);
        // return a newly created element
        assertEquals(list(10), (List<Integer>)v2WithDefault.get(5));
    }
}

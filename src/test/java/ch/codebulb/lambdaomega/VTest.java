package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.list;
import static ch.codebulb.lambdaomega.TestUtil.*;
import static ch.codebulb.lambdaomega.V2.v;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
    public void testGet() {
        assertEquals(0, v(0, "a").get0());
        int x;
        x = v(0, "a").get(0);
        assertEquals(0, x);
        assertEquals("a", v(0, "a").get1());
        String y = v(0, "a").get(1);
        assertEquals("a", y);
    }
    
    @Test
    public void testSet() {
        assertEquals(v(0, "a"), v(1, "a").set0(0));
        assertEquals(v(0, "a"), v(1, "a").set(0, 0));
        
        assertEquals(v(0, "a"), v(0, "b").set1("a"));
        assertEquals(v(0, "a"), v(0, "b").set(1, "a"));
    }
    
    @Test
    public void testToList() {
        assertEquals(list(v(0, "a"), v(1, "b"), v(1, "z")), v(0, "a").a(1, "b").a(1, "z").l);
    }
}

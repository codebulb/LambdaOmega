package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.*;
import static ch.codebulb.lambdaomega.M.*;
import static ch.codebulb.lambdaomega.TestUtil.*;
import ch.codebulb.lambdaomega.abstractions.I;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Basic test cases for {@link M}.
 */
public class MBaseTest {
    @Test
    public void testObject() {
        assertTrue(m().equals(m()));
        assertTrue(m("a", 0).equals(m("a", 0)));
        assertFalse(m("a", 1).equals(m("a", 0)));
        assertTrue(EXPECTED_MAP.equals(m("a", 0).i("b", 1).i("c", 2).m));
        
        assertEquals("M{a=0, b=1, c=2}", "M" + EXPECTED_MAP.toString(), m("a", 0).i("b", 1).i("c", 2).toString());
    }
    
    @Test
    public void testStream() {
        assertFalse(m().stream().isParallel());
        assertFalse(m().Seq().stream().isParallel());
        assertTrue(m().Par().stream().isParallel());
    }
    
    @Test
    public void testWithDefault() {
        I<String, List<String>> mapWithDefault = m("a", list("a?")).i("b", list("b?")).i("c", list("c?")).WithDefault(it -> list(it + '!'));
        assertEquals(list("a?"), mapWithDefault.get("a"));
        assertEquals(list("e!"), mapWithDefault.get("e"));
        
        mapWithDefault.get("a").add("y");
        // keep previously created element
        assertEquals(list("a?", "y"), mapWithDefault.get("a"));
        mapWithDefault.get("e").add("z");
        // original entry has been changed
        assertEquals(list("e!", "z"), mapWithDefault.get("e"));
        
        // usage without explicit map
        mapWithDefault = m(it -> list(it + '!'));
        assertEquals(list("b!"), mapWithDefault.get("b"));
        
        mapWithDefault.get("a").add("y");
        // keep previously created element
        assertEquals(list("a!", "y"), mapWithDefault.get("a"));
        mapWithDefault.get("e").add("z");
        // original entry has been changed
        assertEquals(list("e!", "z"), mapWithDefault.get("e"));
    }
    
    @Test
    public void testE() {
        assertTrue(e("a", 1).equals(e("a", 1)));
        assertFalse(e("a", 1).equals(e("a", 2)));
        assertFalse(e("a", 1).equals(e("b", 1)));
        
        assertEquals("e(a, 1)", e("a", 1).toString());
        
        assertEquals("a", e("a", 1).k);
        assertEquals("a", e("a", 1).getK());
        assertEquals(1, e("a", 1).v);
        assertEquals(1, e("a", 1).getV());
        
        assertEquals("a", e("a", 1).toEntry().getKey());
        assertEquals(1, e("a", 1).toEntry().getValue());
    }
}

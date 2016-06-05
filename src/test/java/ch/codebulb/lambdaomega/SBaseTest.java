package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.S.*;
import static ch.codebulb.lambdaomega.TestUtil.EXPECTED_NESTED_SET;
import static ch.codebulb.lambdaomega.TestUtil.EXPECTED_SET;
import static ch.codebulb.lambdaomega.TestUtil.assertEquals;
import java.util.LinkedHashSet;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Basic test cases for {@link S}.
 */
public class SBaseTest {
    @Test
    public void testObject() {
        assertTrue(s().equals(s()));
        assertTrue(s(0, 1, 2).equals(s(0, 1, 2)));
        assertTrue(s(2, 0, 1).equals(s(0, 1, 2)));
        assertTrue(s(2, 0, 1, 2, 0, 1).equals(s(0, 1, 2)));
        assertFalse(s(9, 1, 2).equals(s(0, 1, 2)));
        assertTrue(set(0, 1, 2).equals(s(0, 1, 2).s));
        
        assertEquals("S[0, 1, 2]", "S" + EXPECTED_SET.toString(), s(0, 1, 2).toString());
    }
    
    @Test
    public void testStream() {
        assertFalse(s().stream().isParallel());
        assertFalse(s().Seq().stream().isParallel());
        assertTrue(s().Par().stream().isParallel());
    }
    
    @Test
    public void testLiteralConstruction() {
        assertEquals(S.class, s().getClass());
        assertEquals(new LinkedHashSet<Integer>(), s().s);
        assertEquals(new LinkedHashSet<Integer>(), s().toSet());
        assertEquals(new LinkedHashSet<Integer>(), s().to(LinkedHashSet.class));
        assertEquals(new LinkedHashSet<Integer>(), set());
        
        assertEquals(EXPECTED_SET, s(0, 1, 2).s);
        assertEquals(EXPECTED_SET, set(0, 1, 2));
        
        assertEquals(EXPECTED_SET, S(EXPECTED_SET).s);
        assertEquals(EXPECTED_SET, s(new Integer[]{0, 1, 2}).s);
        
        assertEquals(EXPECTED_SET, s(Integer.class).a(0, 1, 2).s);
        
        assertEquals(EXPECTED_NESTED_SET, s(s(0, 1, 2).s, s(3, 4, 5).s).s);
        assertEquals(EXPECTED_NESTED_SET, S(s(0, 1, 2), s(3, 4, 5)).s);
        
        assertEquals(EXPECTED_SET, s(3, it -> it).s);
        assertEquals(EXPECTED_SET, set(3, it -> it));
    }
}

package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.S.*;
import static ch.codebulb.lambdaomega.TestUtil.*;
import ch.codebulb.lambdaomega.abstractions.SequentialI;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test cases for the {@link S} implementation for {@link SequentialI}.
 */
public class SBaseSequentialTest {
    @Test
    public void testConvert() {
        assertEquals(set("a", "b", "c"), s("a", "b", "c").toSet());
    }
    
    @Test
    public void testCollection() {
        assertEquals(0, s().size());
        assertEquals(3, s(0, 1, 2).size());
        
        assertTrue(s().isEmpty());
        assertFalse(s(0, 1, 2).isEmpty());
        
        assertEquals(set(), s("a", "b", "c").clear());
    }
    
    @Test
    public void testAdd() {
        // Add individual elements
        assertEquals(EXPECTED_SET, s(0, 1).Add(2).s);
        assertEquals(EXPECTED_SET, s(0, 1).a(2).s);
        assertEquals(EXPECTED_SET, s(0, 1).add(2));
        
        assertEquals(EXPECTED_SET, s(Number.class).Add(0, 1, 2).s);
        assertEquals(EXPECTED_SET, s(Number.class).a(0, 1, 2).s);
        assertEquals(EXPECTED_SET, s(Number.class).add(0, 1, 2));
        
        // Add nested sets
        assertEquals(EXPECTED_NESTED_SET, s(set(0, 1, 2)).Add(set(3, 4, 5)).s);
        assertEquals(EXPECTED_NESTED_SET, s(set(0, 1, 2)).a(set(3, 4, 5)).s);
        assertEquals(EXPECTED_NESTED_SET, s(set(0, 1, 2)).add(set(3, 4, 5)));
        
        assertEquals(EXPECTED_NESTED_SET, s().Add(set(0, 1, 2), set(3, 4, 5)).s);
        assertEquals(EXPECTED_NESTED_SET, s().a(set(0, 1, 2), set(3, 4, 5)).s);
        assertEquals(EXPECTED_NESTED_SET, s().add(set(0, 1, 2), set(3, 4, 5)));
        
        // Add nested Ss
        assertEquals(set(s(0, 1, 2), s(3, 4, 5)), s(s(0, 1, 2)).Add(s(3, 4, 5)).s);
        assertEquals(set(s(0, 1, 2), s(3, 4, 5)), s(s(0, 1, 2)).a(s(3, 4, 5)).s);
        assertEquals(set(s(0, 1, 2), s(3, 4, 5)), s(s(0, 1, 2)).add(s(3, 4, 5)));
        
        assertEquals(set(s(0, 1, 2), s(3, 4, 5)), s().Add(s(0, 1, 2), s(3, 4, 5)).s);
        assertEquals(set(s(0, 1, 2), s(3, 4, 5)), s().a(s(0, 1, 2), s(3, 4, 5)).s);
        assertEquals(set(s(0, 1, 2), s(3, 4, 5)), s().add(s(0, 1, 2), s(3, 4, 5)));
        
        // Add all entries
        assertEquals(EXPECTED_SET, s(0).AddAll(set(1, 2)).s);
        assertEquals(EXPECTED_SET, s(0).A(set(1, 2)).s);
        assertEquals(EXPECTED_SET, s(0).addAll(set(1, 2)));
        
        assertEquals(EXPECTED_SET, s(Number.class).AddAll(set(0), set(1, 2)).s);
        assertEquals(EXPECTED_SET, s(Number.class).A(set(0), set(1, 2)).s);
        assertEquals(EXPECTED_SET, s(Number.class).addAll(set(0), set(1, 2)));
        
        // Add all S entries
        assertEquals(EXPECTED_SET, s(0).AddAll(s(1, 2)).s);
        assertEquals(EXPECTED_SET, s(0).A(s(1, 2)).s);
        assertEquals(EXPECTED_SET, s(0).addAll(s(1, 2)));
        
        assertEquals(EXPECTED_SET, s(Number.class).AddAll(s(0), s(1, 2)).s);
        assertEquals(EXPECTED_SET, s(Number.class).A(s(0), s(1, 2)).s);
        assertEquals(EXPECTED_SET, s(Number.class).addAll(s(0), s(1, 2)));
    }
    
    @Test
    public void testRemove() {
        // Remove individual entries
        assertEquals(EXPECTED_SET, s(3, 0, 1, 2).Remove(3).s);
        assertEquals(EXPECTED_SET, s(3, 0, 1, 2).r(3).s);
        assertEquals(EXPECTED_SET, s(3, 0, 1, 2).remove(3));
        
        // Remove all entries
        assertEquals(EXPECTED_SET, s(3, 4, 5, 0, 1, 2).RemoveAll(set(3), set(4, 5)).s);
        assertEquals(EXPECTED_SET, s(3, 4, 5, 0, 1, 2).R(set(3), set(4, 5)).s);
        assertEquals(EXPECTED_SET, s(3, 4, 5, 0, 1, 2).removeAll(set(3), set(4, 5)));
        
        assertEquals(EXPECTED_SET, s(3, 4, 5, 0, 1, 2).RemoveAll(s(3), s(4, 5)).s);
        assertEquals(EXPECTED_SET, s(3, 4, 5, 0, 1, 2).R(s(3), s(4, 5)).s);
        assertEquals(EXPECTED_SET, s(3, 4, 5, 0, 1, 2).removeAll(s(3), s(4, 5)));
        
        assertEquals(EXPECTED_SET, s(3, 4, 5, 0, 1, 2).retainAll(set(0), set(1, 2)));
    }
    
    @Test
    public void testGet() {
        assertEquals(true, s("a", "b", "c").contains("a"));
        assertEquals(true, s("a", "b", "c").contains("a", "c"));
        assertEquals(false, s("a", "b", "c").contains("a", "d"));
        
        assertEquals(true, s("a", "b", "c").containsAll(set("a", "b"), set("c")));
        assertEquals(false, s("a", "b", "c").containsAll(set("a", "b"), set("c", "d")));
    }
}

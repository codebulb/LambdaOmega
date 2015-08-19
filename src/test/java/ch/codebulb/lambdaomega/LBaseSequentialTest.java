package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.*;
import static ch.codebulb.lambdaomega.R.r;
import static ch.codebulb.lambdaomega.S.set;
import static ch.codebulb.lambdaomega.TestUtil.*;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for the {@link L} implementation for {@link SequentialI}.
 */
public class LBaseSequentialTest {
    @BeforeClass
    public static void beforeClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = true;
    }
    
    @Test
    public void testConvert() {
        assertEquals(list("a", "b", "c"), l("a", "b", "c").toList());
        
        assertArrayEquals(new Object[]{"a", "b", "c"}, l("a", "b", "c").toArray());
        assertArrayEquals(new String[]{"a", "b", "c"}, l("a", "b", "c").toArray(new String[3]));
        assertArrayEquals(new String[]{"a", "b", "c"}, l("a", "b", "c").toArray(s -> new String[s]));
        
        assertEquals(set("a", "b", "c"), l("a", "b", "c").toSet());
        
        assertEquals(r(0).to(3), l("a", "b", "c").toRange());
    }
    
    @Test
    public void testCollection() {
        assertEquals(0, l().size());
        assertEquals(3, l(0, 1, 2).size());
        
        assertTrue(l().isEmpty());
        assertFalse(l(0, 1, 2).isEmpty());
        
        assertEquals(list(), l("a", "b", "c").clear());
    }
    
    @Test
    public void testAdd() {
        // Add individual elements
        assertEquals(EXPECTED_LIST, l(0, 1).Add(2).l);
        assertEquals(EXPECTED_LIST, l(0, 1).a(2).l);
        assertEquals(EXPECTED_LIST, l(0, 1).add(2));
        
        assertEquals(EXPECTED_LIST, l(Number.class).Add(0, 1, 2).l);
        assertEquals(EXPECTED_LIST, l(Number.class).a(0, 1, 2).l);
        assertEquals(EXPECTED_LIST, l(Number.class).add(0, 1, 2));
        
        // Add nested lists
        assertEquals(EXPECTED_NESTED_LIST, l(list(0, 1, 2)).Add(list(0, 1, 2)).l);
        assertEquals(EXPECTED_NESTED_LIST, l(list(0, 1, 2)).a(list(0, 1, 2)).l);
        assertEquals(EXPECTED_NESTED_LIST, l(list(0, 1, 2)).add(list(0, 1, 2)));
        
        assertEquals(EXPECTED_NESTED_LIST, l().Add(list(0, 1, 2), list(0, 1, 2)).l);
        assertEquals(EXPECTED_NESTED_LIST, l().a(list(0, 1, 2), list(0, 1, 2)).l);
        assertEquals(EXPECTED_NESTED_LIST, l().add(list(0, 1, 2), list(0, 1, 2)));
        
        // Add nested Ls
        assertEquals(list(l(0, 1, 2), l(0, 1, 2)), l(l(0, 1, 2)).Add(l(0, 1, 2)).l);
        assertEquals(list(l(0, 1, 2), l(0, 1, 2)), l(l(0, 1, 2)).a(l(0, 1, 2)).l);
        assertEquals(list(l(0, 1, 2), l(0, 1, 2)), l(l(0, 1, 2)).add(l(0, 1, 2)));
        
        assertEquals(list(l(0, 1, 2), l(0, 1, 2)), l().Add(l(0, 1, 2), l(0, 1, 2)).l);
        assertEquals(list(l(0, 1, 2), l(0, 1, 2)), l().a(l(0, 1, 2), l(0, 1, 2)).l);
        assertEquals(list(l(0, 1, 2), l(0, 1, 2)), l().add(l(0, 1, 2), l(0, 1, 2)));
        
        // Add all entries
        assertEquals(EXPECTED_LIST, l(0).AddAll(list(1, 2)).l);
        assertEquals(EXPECTED_LIST, l(0).A(list(1, 2)).l);
        assertEquals(EXPECTED_LIST, l(0).addAll(list(1, 2)));
        
        assertEquals(EXPECTED_LIST, l(Number.class).AddAll(list(0), list(1, 2)).l);
        assertEquals(EXPECTED_LIST, l(Number.class).A(list(0), list(1, 2)).l);
        assertEquals(EXPECTED_LIST, l(Number.class).addAll(list(0), list(1, 2)));
        
        // Add all L entries
        assertEquals(EXPECTED_LIST, l(0).AddAll(l(1, 2)).l);
        assertEquals(EXPECTED_LIST, l(0).A(l(1, 2)).l);
        assertEquals(EXPECTED_LIST, l(0).addAll(l(1, 2)));
        
        assertEquals(EXPECTED_LIST, l(Number.class).AddAll(l(0), l(1, 2)).l);
        assertEquals(EXPECTED_LIST, l(Number.class).A(l(0), l(1, 2)).l);
        assertEquals(EXPECTED_LIST, l(Number.class).addAll(l(0), l(1, 2)));
        
        // Add at index
        assertEquals(EXPECTED_LIST, l(0, 2).addAt(1, 1));
        assertEquals(EXPECTED_LIST, l(2).addAt(0, 0, 1));
        assertEquals(EXPECTED_LIST, l(Number.class).a(2).addAt(0, 0, 1));
        
        assertEquals(EXPECTED_LIST, l(0, 1).addAt(2, 2));
        try {
            l(0, 1).addAt(3, 3);
            fail();
        }
        catch (IndexOutOfBoundsException ex) {}
        
        assertEquals(EXPECTED_LIST, l(2).addAllAt(0, list(0, 1)));
        assertEquals(EXPECTED_LIST, l(2).addAllAt(0, list(0), list(1)));
        assertEquals(EXPECTED_LIST, l(Number.class).a(2).addAllAt(0, list(0), list(1)));
    }
    
    @Test
    public void testRemove() {
        // Remove individual entries
        assertEquals(EXPECTED_LIST, l(3, 0, 1, 2).Remove(3).l);
        assertEquals(EXPECTED_LIST, l(3, 0, 1, 2).r(3).l);
        assertEquals(EXPECTED_LIST, l(3, 0, 1, 2).remove(3));
        
        // Remove all entries
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).RemoveAll(list(3), list(4, 5)).l);
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).R(list(3), list(4, 5)).l);
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).removeAll(list(3), list(4, 5)));
        
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).RemoveAll(l(3), l(4, 5)).l);
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).R(l(3), l(4, 5)).l);
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).removeAll(l(3), l(4, 5)));
        
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).retainAll(list(0), list(1, 2)));
    }
    
    @Test
    public void testGet() {
        assertEquals(true, l("a", "b", "c").contains("a"));
        assertEquals(true, l("a", "b", "c").contains("a", "c"));
        assertEquals(false, l("a", "b", "c").contains("a", "d"));
        
        assertEquals(true, l("a", "b", "c").containsAll(list("a", "b"), list("c")));
        assertEquals(false, l("a", "b", "c").containsAll(list("a", "b"), list("c", "d")));
        
        assertEquals(3, l("a", "b", "c", "a").lastIndexOf("a"));
        assertEquals(-1, l("a", "b", "c").lastIndexOf("d"));
        
        assertEquals(list("b", "c"), list("a", "b", "c", "d").subList(1, 3), l("a", "b", "c", "d").subList(1, 3));
    }
    
    @AfterClass
    public static void afterClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = false;
    }
}

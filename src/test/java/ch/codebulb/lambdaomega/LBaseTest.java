package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.*;
import static ch.codebulb.lambdaomega.M.*;
import static ch.codebulb.lambdaomega.R.r;
import static ch.codebulb.lambdaomega.TestUtil.*;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

// TODO Restructure tests to include List-as-IndexedI tests
public class LBaseTest {
    @BeforeClass
    public static void beforeClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = true;
    }
    
    @Test
    public void testBasics() {
        assertTrue(l().equals(l()));
        assertTrue(l(0, 1, 2).equals(l(0, 1, 2)));
        assertFalse(l(9, 1, 2).equals(l(0, 1, 2)));
        assertTrue(EXPECTED_LIST.equals(list(0, 1, 2)));
        assertTrue(EXPECTED_LIST.equals(l(0, 1, 2).l));
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
    public void testConvert() {
        assertEquals(r(0).to(3), l("a", "b", "c").toRange());
    }
    
    @Test
    public void testCC() {
        assertEquals(0, l().size());
        assertEquals(3, l(0, 1, 2).size());
        
        assertTrue(l().isEmpty());
        assertFalse(l(0, 1, 2).isEmpty());
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
        
        // Add all at index
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
        
        // Put at index
        L<Number> putList = l(3, 1, 2);
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, putList.put(0, 0), putList.toInternalMap());
        assertEquals(EXPECTED_LIST, l(3, 1, 2).Put(0, 0).l);
        try {
            putList.put(3, 3);
            fail();
        }
        catch (IndexOutOfBoundsException ex) {}
        
        putList = l(null, 1, 2);
        assertEquals(null, putList.putIfAbsent(0, 0));
        assertEquals(EXPECTED_LIST, putList.l);
        assertEquals(1, putList.putIfAbsent(1, 3));
        assertEquals(EXPECTED_LIST, putList.l);
        
        L<String> putAllList = l("x", "y", "c");
        putAllList.putAll(m(Integer.class, String.class).i(0, "a").i(1, "b").m);
        assertEquals(list("a", "b", "c"), putAllList.l);
        
        assertEquals(m(0, "a").i(1, "b").m, l("a", "z").putAll(m(1, "b")));
        assertEquals(list("a", "b"), l("a", "z").PutAll(m(1, "b")).l);
        assertEquals(list("a", "b"), l("a", "z").P(m(1, "b")).l);
        
        assertEquals(m(0, "a").i(1, "b").i(2, "c").i(3, "d").m, l("x", "y", "z", "d").putAll(m(0, "a").i(1, "b"), m(2, "c")));
        assertEquals(list("a", "b", "c", "d"), l("x", "y", "z", "d").PutAll(m(0, "a").i(1, "b"), m(2, "c")).l);
        assertEquals(list("a", "b", "c", "d"), l("x", "y", "z", "d").P(m(0, "a").i(1, "b"), m(2, "c")).l);
        
        // Set / replace at index
        L<Number> replaceList = l(3, 1, 2);
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, replaceList.set(0, 0), replaceList.toInternalMap());
        assertEquals(EXPECTED_LIST, l(3, 1, 2).Set(0, 0).l);
        assertEquals(EXPECTED_LIST, l(3, 1, 2).s(0, 0).l);
        try {
            replaceList.set(3, 3);
            fail();
        }
        catch (IndexOutOfBoundsException ex) {}
        
        replaceList = l(3, 1, 2);
        assertEquals(3, replaceList.replace(0, 0));
        assertEquals(EXPECTED_LIST, replaceList.l);
        assertEquals(null, replaceList.replace(3, 3));
        assertEquals(EXPECTED_LIST, replaceList.l);
        
        replaceList = l(3, 1, 2);
        assertEquals(true, replaceList.replace(0, 3, 0));
        assertEquals(EXPECTED_LIST, replaceList.l);
        assertEquals(false, replaceList.replace(1, 4, 5));
        assertEquals(EXPECTED_LIST, replaceList.l);
    }
    
    @Test
    public void testRemove() {
        // Remove individual elements
        assertEquals(EXPECTED_LIST, l(3, 0, 1, 2).Remove(3).l);
        assertEquals(EXPECTED_LIST, l(3, 0, 1, 2).r(3).l);
        assertEquals(EXPECTED_LIST, l(3, 0, 1, 2).remove(3));
        
        // Remove individual elements at index
        assertEquals(EXPECTED_LIST, l(4, 3, 0, 1, 2).DeleteKey(0, 1).l);
        assertEquals(EXPECTED_LIST, l(4, 3, 0, 1, 2).d(0, 1).l);
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(4, 3, 0, 1, 2).deleteKey(0, 1));
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(3, 4, 0, 1, 2).deleteKey(0, 1));
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(3, 4, 0, 1, 2).deleteKey(1, 0));
        L<Integer> deleteList = l(3, 0, 1, 2);
        assertEquals(true, deleteList.deleteIfMatches(0, 3));
        assertEquals(EXPECTED_LIST, deleteList.l);
        
        // Remove all entries
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).removeAll(list(3), list(4, 5)));
        
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).DeleteAllKeys(list(0), list(1, 2)).l);
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).D(list(0), list(1, 2)).l);
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(3, 4, 5, 0, 1, 2).deleteAllKeys(list(0), list(1, 2)));
        
        L<Number> removeList = l(3, 0, 1, 2);
        assertEquals(true, removeList.deleteIfMatches(0, 3));
        assertEquals(EXPECTED_LIST, removeList.l);
        assertEquals(false, removeList.deleteIfMatches(1, 4));
        assertEquals(EXPECTED_LIST, removeList.l);
    }
    
    @Test
    public void testGet() {
        assertEquals("a", l("a", "b", "c").get(0));
        assertEquals(list("a", "c"), l("a", "b", "c").get(0, 2));
        assertEquals(list("a", "c"), l("a", "b", "c").Get(0, 2).l);
        assertEquals(list("a", "c"), l("a", "b", "c").g(0, 2).l);
        assertEquals("a", l("a", "b", "c").g(0));
        assertEquals(list("b", "c"), l("a", "b", "c").get(r(1).to(2)));
        assertEquals(list("b", "c"), l("a", "b", "c").Get(r(1).to(2)).l);
        
        assertEquals("a", l("a", "b", "c").getOrDefault(0, "d"));
        assertEquals("d", l("a", "b", "c").getOrDefault(4, "d"));
        assertEquals("d", l("a", "b", "c").getOrDefault(-1, "d"));
        
        assertEquals(true, l("a", "b", "c").containsValue("a"));
        assertEquals(false, l("a", "b", "c").containsValue("d"));
        assertEquals(true, l("a", "b", "c").containsKey(0));
        assertEquals(false, l("a", "b", "c").containsKey(4));
        assertEquals(false, l("a", "b", "c").containsKey(-1));
        
        assertEquals(0, l("a", "b", "c", "a").indexOf("a"));
        assertEquals(-1, l("a", "b", "c").indexOf("d"));
        
        assertEquals(3, l("a", "b", "c", "a").lastIndexOf("a"));
        assertEquals(-1, l("a", "b", "c").lastIndexOf("d"));
    }
    
    @Test
    public void testWithDefault() {
        L<List<Integer>> listWithDefault = l(list(5), list(6), list(7)).WithDefault(it -> list(it * 2));
        assertEquals(list(5), listWithDefault.get(0));
        assertEquals(list(10), listWithDefault.get(5));
        
        listWithDefault.get(0).add(9);
        assertEquals(list(5, 9), listWithDefault.get(0));
        listWithDefault.get(5).add(9);
        // return a newly created element
        assertEquals(list(10), listWithDefault.get(5));
    }
    
    @Test
    public void testMapAccess() {
        assertEquals(EXPECTED_LIST, l(0, 1, 2).getValues());
        assertEquals(EXPECTED_LIST, C.toList(l("a", "b", "c").getKeys()));
    }
    
    @AfterClass
    public static void afterClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = false;
    }
}

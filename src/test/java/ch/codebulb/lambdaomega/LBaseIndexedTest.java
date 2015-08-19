package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.l;
import static ch.codebulb.lambdaomega.L.list;
import static ch.codebulb.lambdaomega.M.e;
import static ch.codebulb.lambdaomega.M.m;
import static ch.codebulb.lambdaomega.R.r;
import static ch.codebulb.lambdaomega.S.set;
import static ch.codebulb.lambdaomega.TestUtil.EXPECTED_LIST;
import static ch.codebulb.lambdaomega.TestUtil.assertEquals;
import ch.codebulb.lambdaomega.abstractions.IndexedI;
import ch.codebulb.lambdaomega.abstractions.IndexedListI.IndexAlreadyPresentException;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for the {@link L} implementation for {@link IndexedI}.
 */
public class LBaseIndexedTest {
    @BeforeClass
    public static void beforeClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = true;
    }
    
    @Test
    public void testAdd() {
        // Add individual elements
        assertEquals(list("a", "b"), l("a").Insert(1, "b").l);
        assertEquals(list("a", "b"), l("a").i(1, "b").l);
        assertEquals(m(0, "a").i(1, "b").m, l("a").insert(1, "b"));
        
        L<String> addList = l("a").i(1, "b");
        try {
            addList.Insert(1, "c");
            fail();
        }
        catch (IndexAlreadyPresentException ex) {
            assertEquals(1, ex.key);
            assertEquals("b", ex.previousValue);
        }
        assertEquals(list("a", "b"), addList.l);
        addList = l("a").i(1, "b");
        try {
            addList.insert(1, "c");
            fail();
        }
        catch (IndexAlreadyPresentException ex)  {
            assertEquals(1, ex.key);
            assertEquals("b", ex.previousValue);
        }
        assertEquals(list("a", "b"), addList.l);
        addList = l("a").i(1, "b");
        try {
            addList.i(1, "c");
            fail();
        }
        catch (IndexAlreadyPresentException ex)  {
            assertEquals(1, ex.key);
            assertEquals("b", ex.previousValue);
        }
        assertEquals(list("a", "b"), addList.l);
        
        // Add all entries
        assertEquals(list("a", "b", "c"), l("a").InsertAll(m(1, "b").i(2, "c").m).l);
        assertEquals(list("a", "b", "c"), l("a").I(m(1, "b").i(2, "c").m).l);
        assertEquals(m(0, "a").i(1, "b").i(2, "c").m, l("a").insertAll(m(1, "b").i(2, "c").m));
        
        addList = l("a").i(1, "b");
        try {
            addList.InsertAll(m(1, "x").i(2, "y").m, m(0, "z").m);
            fail();
        }
        catch (IndexAlreadyPresentException ex) {
            assertEquals(1, ex.key);
            assertEquals("b", ex.previousValue);
        }
        assertEquals(list("a", "b"), addList.l);
        addList = l("a").i(1, "b");
        try {
            addList.insertAll(m(1, "x").i(2, "y").m, m(0, "z").m);
            fail();
        }
        catch (IndexAlreadyPresentException ex) {
            assertEquals(1, ex.key);
            assertEquals("b", ex.previousValue);
        }
        assertEquals(list("a", "b"), addList.l);
        addList = l("a").i(1, "b");
        try {
            addList.I(m(1, "x").i(2, "y").m, m(0, "z").m);
            fail();
        }
        catch (IndexAlreadyPresentException ex) {
            assertEquals(1, ex.key);
            assertEquals("b", ex.previousValue);
        }
        assertEquals(list("a", "b"), addList.l);
        
        addList = l("a").i(1, "b");
        try {
            addList.InsertAll(m(2, "x").i(3, "y").m, m(3, "z").m);
            fail();
        }
        catch (IndexAlreadyPresentException ex) {
            assertEquals(3, ex.key);
            assertEquals("y", ex.previousValue);
        }
        assertEquals(list("a", "b"), addList.l);
        addList = l("a").i(1, "b");
        try {
            addList.insertAll(m(2, "x").i(3, "y").m, m(3, "z").m);
            fail();
        }
        catch (IndexAlreadyPresentException ex) {
            assertEquals(3, ex.key);
            assertEquals("y", ex.previousValue);
        }
        assertEquals(list("a", "b"), addList.l);
        addList = l("a").i(1, "b");
        try {
            addList.I(m(2, "x").i(3, "y").m, m(3, "z").m);
            fail();
        }
        catch (IndexAlreadyPresentException ex) {
            assertEquals(3, ex.key);
            assertEquals("y", ex.previousValue);
        }
        assertEquals(list("a", "b"), addList.l);
        
        // Add all M entries
        assertEquals(list("a", "b", "c"), l("a").InsertAll(m(1, "b").i(2, "c")).l);
        assertEquals(list("a", "b", "c"), l("a").I(m(1, "b").i(2, "c")).l);
        assertEquals(m(0, "a").i(1, "b").i(2, "c").m, l("a").insertAll(m(1, "b").i(2, "c")));
        
        addList = l("a").i(1, "b");
        try {
            addList.InsertAll(m(1, "x").i(2, "y").m, m(0, "z").m);
            fail();
        }
        catch (IndexAlreadyPresentException ex) {}
        assertEquals(list("a", "b"), addList.l);
        addList = l("a").i(1, "b");
        try {
            addList.insertAll(m(1, "x").i(2, "y").m, m(0, "z").m);
            fail();
        }
        catch (IndexAlreadyPresentException ex) {}
        assertEquals(list("a", "b"), addList.l);
        addList = l("a").i(1, "b");
        try {
            addList.I(m(1, "x").i(2, "y").m, m(0, "z").m);
            fail();
        }
        catch (IndexAlreadyPresentException ex) {}
        assertEquals(list("a", "b"), addList.l);
        
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
        
        assertEquals(m(0, "a").i(1, "b").m, l("a", "z").putAll(m(1, "b").m));
        assertEquals(list("a", "b"), l("a", "z").PutAll(m(1, "b").m).l);
        assertEquals(list("a", "b"), l("a", "z").P(m(1, "b").m).l);
        
        // Set / replace at index
        assertEquals(EXPECTED_LIST, l(3, 1, 2).Set(0, 0).l);
        assertEquals(EXPECTED_LIST, l(3, 1, 2).s(0, 0).l);
        L<Number> replaceList = l(3, 1, 2);
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, replaceList.set(0, 0), replaceList.toInternalMap());
        try {
            replaceList.set(3, 3);
            fail();
        }
        catch (IndexOutOfBoundsException ex) {}
        
        assertEquals(list("a", "b", "c"), l("a", "y", "z").SetAll(m(1, "b").m, m(2, "c").m).l);
        assertEquals(list("a", "b", "c"), l("a", "y", "z").SetAll(m(1, "b"), m(2, "c")).l);
        assertEquals(list("a", "b", "c"), l("a", "y", "z").S(m(1, "b").m, m(2, "c").m).l);
        assertEquals(list("a", "b", "c"), l("a", "y", "z").S(m(1, "b"), m(2, "c")).l);
        assertEquals(m(0, "a").i(1, "b").i(2, "c").m, l("a", "y", "z").setAll(m(1, "b").m, m(2, "c").m));
        assertEquals(m(0, "a").i(1, "b").i(2, "c").m, l("a", "y", "z").setAll(m(1, "b"), m(2, "c")));
        
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
        // Remove individual entries at index
        assertEquals(EXPECTED_LIST, l(4, 3, 0, 1, 2).DeleteKey(0, 1).l);
        assertEquals(EXPECTED_LIST, l(4, 3, 0, 1, 2).d(0, 1).l);
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(4, 3, 0, 1, 2).deleteKey(0, 1));
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(3, 4, 0, 1, 2).deleteKey(0, 1));
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(3, 4, 0, 1, 2).deleteKey(1, 0));
        
        assertEquals(EXPECTED_LIST, l(3, 4, 0, 1, 2).DeleteValue(3, 4).l);
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(3, 4, 0, 1, 2).deleteValue(3, 4));
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(3, 3, 0, 1, 2).deleteValue(3, 3));
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(3, 4, 0, 1, 2).deleteValue(3, 4, -1));
        
        // Conditional remove
        L<Number> removeList = l(3, 0, 1, 2);
        assertEquals(true, removeList.deleteIfMatches(0, 3));
        assertEquals(EXPECTED_LIST, removeList.l);
        assertEquals(false, removeList.deleteIfMatches(1, 4));
        assertEquals(EXPECTED_LIST, removeList.l);
        
        // Remove all entries
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).DeleteAllKeys(list(0), list(1, 2)).l);
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).D(list(0), list(1, 2)).l);
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(3, 4, 5, 0, 1, 2).deleteAllKeys(list(0), list(1, 2)));
        
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).DeleteAllKeys(l(0), l(1, 2)).l);
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).D(l(0), l(1, 2)).l);
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(3, 4, 5, 0, 1, 2).deleteAllKeys(l(0), l(1, 2)));
        
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).DeleteAllValues(list(3), list(4, 5)).l);
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(3, 4, 5, 0, 1, 2).deleteAllValues(list(3), list(4, 5)));
        
        assertEquals(EXPECTED_LIST, l(3, 4, 5, 0, 1, 2).DeleteAllValues(l(3), l(4, 5)).l);
        assertEquals(m(0, 0).i(1, 1).i(2, 2).m, l(3, 4, 5, 0, 1, 2).deleteAllValues(l(3), l(4, 5)));
    }
    
    @Test
    public void testGet() {
        assertEquals("a", l("a", "b", "c").get(0));
        assertEquals(list("a", "c"), l("a", "b", "c").get(0, 2));
        assertEquals(list("a", "c"), l("a", "b", "c").Get(0, 2).l);
        assertEquals(list("a", "c"), l("a", "b", "c").g(0, 2).l);
        assertEquals(list("b", "c"), l("a", "b", "c").get(r(1).to(2)));
        assertEquals(list("b", "c"), l("a", "b", "c").Get(r(1).to(2)).l);
        
        // get all
        assertEquals(EXPECTED_LIST, l(0, 1, 2).getValues());
        assertEquals(set(0, 1, 2), l("a", "b", "c").getKeys());
        assertEquals(set(e(0, "a"), e(1, "b"), e(2, "c")), l("a", "b", "c").getEntries());
        
        assertEquals("a", l("a", "b", "c").getOrDefault(0, "d"));
        assertEquals("d", l("a", "b", "c").getOrDefault(4, "d"));
        assertEquals("d", l("a", "b", "c").getOrDefault(-1, "d"));
        
        assertEquals(true, l("a", "b", "c").containsAnyValue("a"));
        assertEquals(true, l("a", "b", "c").containsAnyValue("a", "d"));
        assertEquals(false, l("a", "b", "c").containsAnyValue("d"));
        assertEquals(false, l("a", "b", "c").containsAnyValue("d", "e"));
        assertEquals(true, l("a", "b", "c").containsAnyKey(0));
        assertEquals(true, l("a", "b", "c").containsAnyKey(0, 4));
        assertEquals(false, l("a", "b", "c").containsAnyKey(4));
        assertEquals(false, l("a", "b", "c").containsAnyKey(4, 5));
        assertEquals(false, l("a", "b", "c").containsAnyKey(-1));
        
        assertEquals(true, l("a", "b", "c").containsAnyValue(list("a"), list("d")));
        assertEquals(false, l("a", "b", "c").containsAnyValue(list("d"), list("e")));
        assertEquals(true, l("a", "b", "c").containsAnyKey(list(0), list(4)));
        assertEquals(false, l("a", "b", "c").containsAnyKey(list(4), list(5)));
        
        assertEquals(0, l("a", "b", "c", "a").indexOf("a"));
        assertEquals(0, l("a", "b", "c", "a").getKey("a"));
        assertEquals(-1, l("a", "b", "c").indexOf("d"));
        assertEquals(null, l("a", "b", "c").getKey("d"));
    }
    
    @AfterClass
    public static void afterClass() {
        L.TEST_DISABLE_HELPER_MAP_CONVERSION = false;
    }
    
}

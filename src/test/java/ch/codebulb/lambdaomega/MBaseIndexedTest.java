package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.l;
import static ch.codebulb.lambdaomega.L.list;
import static ch.codebulb.lambdaomega.M.e;
import static ch.codebulb.lambdaomega.M.m;
import static ch.codebulb.lambdaomega.S.set;
import static ch.codebulb.lambdaomega.TestUtil.EXPECTED_LIST;
import static ch.codebulb.lambdaomega.TestUtil.EXPECTED_MAP;
import static ch.codebulb.lambdaomega.TestUtil.EXPECTED_MAP_2_ELEMENTS;
import static ch.codebulb.lambdaomega.TestUtil.assertEquals;
import ch.codebulb.lambdaomega.abstractions.IndexedListIAdd;
import java.util.Map;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Test cases for the {@link M} implementation for {@link IndexedI}.
 */
public class MBaseIndexedTest {
    @Test
    public void testAdd() {
        // Add individual elements
        assertEquals(EXPECTED_MAP_2_ELEMENTS, m("a", 0).Insert("b", 1).m);
        assertEquals(EXPECTED_MAP_2_ELEMENTS, m("a", 0).i("b", 1).m);
        assertEquals(EXPECTED_MAP_2_ELEMENTS, m("a", 0).insert("b", 1));
        
        M<String, Integer> addMap = m("a", 0).i("b", 1);
        try {
            addMap.Insert("b", 2);
            fail();
        }
        catch (IndexedListIAdd.IndexAlreadyPresentException ex) {
            assertEquals("b", ex.key);
            assertEquals(1, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.insert("b", 2);
            fail();
        }
        catch (IndexedListIAdd.IndexAlreadyPresentException ex)  {
            assertEquals("b", ex.key);
            assertEquals(1, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.i("b", 2);
            fail();
        }
        catch (IndexedListIAdd.IndexAlreadyPresentException ex)  {
            assertEquals("b", ex.key);
            assertEquals(1, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        
        // Add all entries
        assertEquals(EXPECTED_MAP, m("a", 0).InsertAll(m("b", 1).i("c", 2).m).m);
        assertEquals(EXPECTED_MAP, m("a", 0).I(m("b", 1).i("c", 2).m).m);
        assertEquals(EXPECTED_MAP, m("a", 0).insertAll(m("b", 1).i("c", 2).m));
        
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.InsertAll(m("b", 4).i("c", 2).m, m("a", 3).m);
            fail();
        }
        catch (IndexedListIAdd.IndexAlreadyPresentException ex) {
            assertEquals("b", ex.key);
            assertEquals(1, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.insertAll(m("b", 4).i("c", 2).m, m("a", 3).m);
            fail();
        }
        catch (IndexedListIAdd.IndexAlreadyPresentException ex) {
            assertEquals("b", ex.key);
            assertEquals(1, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.I(m("b", 4).i("c", 2).m, m("a", 3).m);
            fail();
        }
        catch (IndexedListIAdd.IndexAlreadyPresentException ex) {
            assertEquals("b", ex.key);
            assertEquals(1, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.InsertAll(m("c", 2).i("d", 3).m, m("d", 4).m);
            fail();
        }
        catch (IndexedListIAdd.IndexAlreadyPresentException ex) {
            assertEquals("d", ex.key);
            assertEquals(3, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.insertAll(m("c", 2).i("d", 3), m("d", 4));
            fail();
        }
        catch (IndexedListIAdd.IndexAlreadyPresentException ex) {
            assertEquals("d", ex.key);
            assertEquals(3, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.I(m("c", 2).i("d", 3), m("d", 4));
            fail();
        }
        catch (IndexedListIAdd.IndexAlreadyPresentException ex) {
            assertEquals("d", ex.key);
            assertEquals(3, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        
        // Add all M entries
        assertEquals(EXPECTED_MAP, m("a", 0).InsertAll(m("b", 1).i("c", 2)).m);
        assertEquals(EXPECTED_MAP, m("a", 0).I(m("b", 1).i("c", 2)).m);
        assertEquals(EXPECTED_MAP, m("a", 0).insertAll(m("b", 1).i("c", 2)));
        
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.InsertAll(m("b", 1).i("c", 2), m("a", 3));
            fail();
        }
        catch (IndexedListIAdd.IndexAlreadyPresentException ex) {}
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.insertAll(m("b", 1).i("c", 2), m("a", 3));
            fail();
        }
        catch (IndexedListIAdd.IndexAlreadyPresentException ex) {}
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.I(m("b", 1).i("c", 2), m("a", 3));
            fail();
        }
        catch (IndexedListIAdd.IndexAlreadyPresentException ex) {}
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        
        // Put at index
        M<String, Number> putMap = m(String.class, Number.class).i("a", 3).i("b", 1).i("c", 2);
        assertEquals(EXPECTED_MAP, putMap.put("a", 0), putMap.m);
        assertEquals(EXPECTED_MAP, m("a", 3).i("b", 1).i("c", 2).Put("a", 0).m);
        
        putMap = m(String.class, Number.class).i("a", null).i("b", 1).i("c", 2);
        assertEquals(null, putMap.putIfAbsent("a", 0));
        assertEquals(EXPECTED_MAP, putMap.m);
        assertEquals(1, putMap.putIfAbsent("b", 3));
        assertEquals(EXPECTED_MAP, putMap.m);
        
        assertEquals(EXPECTED_MAP, m("a", 0).PutAll(m("b", 1).m, m("c", 2).m).m);
        assertEquals(EXPECTED_MAP, m("a", 0).P(m("b", 1).m, m("c", 2).m).m);
        assertEquals(EXPECTED_MAP, m("a", 0).putAll(m("b", 1).m, m("c", 2).m));
        
        assertEquals(EXPECTED_MAP, m("a", 0).PutAll(m("b", 1), m("c", 2)).m);
        assertEquals(EXPECTED_MAP, m("a", 0).P(m("b", 1), m("c", 2)).m);
        assertEquals(EXPECTED_MAP, m("a", 0).putAll(m("b", 1), m("c", 2)));
        
        // Set / replace
        assertEquals(EXPECTED_MAP, m("a", 3).i("b", 1).i("c", 2).Set("a", 0).m);
        assertEquals(EXPECTED_MAP, m("a", 3).i("b", 1).i("c", 2).s("a", 0).m);
        M<String, Number> replaceMap = m(String.class, Number.class).i("a", 3).i("b", 1).i("c", 2);
        assertEquals(EXPECTED_MAP, replaceMap.set("a", 0), replaceMap.m);
        
        assertEquals(EXPECTED_MAP, m("a", 0).SetAll(m("b", 1).m, m("c", 2).m).m);
        assertEquals(EXPECTED_MAP, m("a", 0).SetAll(m("b", 1), m("c", 2)).m);
        assertEquals(EXPECTED_MAP, m("a", 0).S(m("b", 1).m, m("c", 2).m).m);
        assertEquals(EXPECTED_MAP, m("a", 0).S(m("b", 1), m("c", 2)).m);
        assertEquals(EXPECTED_MAP, m("a", 0).setAll(m("b", 1).m, m("c", 2).m));
        assertEquals(EXPECTED_MAP, m("a", 0).setAll(m("b", 1), m("c", 2)));
        
        replaceMap = m(String.class, Number.class).i("a", 3).i("b", 1).i("c", 2);
        Map<String, Number> replaceMapExpected = m(String.class, Number.class).i("a", 3).i("b", 1).i("c", 2).m;
        assertEquals(3, replaceMapExpected.replace("a", 0), replaceMap.replace("a", 0));
        assertEquals(EXPECTED_MAP, replaceMapExpected, replaceMap.m);
        assertEquals(null, replaceMapExpected.replace("d", 3), replaceMap.replace("d", 3));
        assertEquals(EXPECTED_MAP, replaceMapExpected, replaceMap.m);
        
        replaceMap = m(String.class, Number.class).i("a", 3).i("b", 1).i("c", 2);
        replaceMapExpected = m(String.class, Number.class).i("a", 3).i("b", 1).i("c", 2).m;
        assertEquals(true, replaceMapExpected.replace("a", 3, 0), replaceMap.replace("a", 3, 0));
        assertEquals(EXPECTED_MAP, replaceMapExpected, replaceMap.m);
        assertEquals(false, replaceMapExpected.replace("d", 4, 5), replaceMap.replace("d", 4, 5));
        assertEquals(EXPECTED_MAP, replaceMapExpected, replaceMap.m);
    }
    
    @Test
    public void testRemove() {
        // Remove individual entries at index
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("a", 0).i("b", 1).i("c", 2).DeleteKey("d", "e").m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("a", 0).i("b", 1).i("c", 2).d("d", "e").m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("a", 0).i("b", 1).i("c", 2).deleteKey("d", "e"));
        
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("a", 0).i("b", 1).i("c", 2).DeleteValue(3, 4).m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("a", 0).i("b", 1).i("c", 2).deleteValue(3, 4));
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 3).i("a", 0).i("b", 1).i("c", 2).deleteValue(3, 3));
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("a", 0).i("b", 1).i("c", 2).deleteValue(3, 4, -1));
        
        // Conditional remove
        M<String, Number> removeMap = m(String.class, Number.class).i("d", 3).i("a", 0).i("b", 1).i("c", 2);
        Map<String, Number> removeMapExpected = m(String.class, Number.class).i("d", 3).i("a", 0).i("b", 1).i("c", 2).m;
        assertEquals(true, removeMap.deleteIfMatches("d", 3));
        assertEquals(true, removeMapExpected.remove("d", 3));
        assertEquals(EXPECTED_MAP, removeMap.m);
        assertEquals(false, removeMap.deleteIfMatches("a", 4));
        assertEquals(false, removeMapExpected.remove("a", 4));
        assertEquals(EXPECTED_MAP, removeMapExpected);
        
        // Remove all entries
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).DeleteAllKeys(list("d"), list("e", "f")).m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).D(list("d"), list("e", "f")).m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).deleteAllKeys(list("d"), list("e", "f")));
        
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).DeleteAllKeys(l("d"), l("e", "f")).m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).D(l("d"), l("e", "f")).m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).deleteAllKeys(l("d"), l("e", "f")));
        
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).DeleteAllValues(list(3), list(4, 5)).m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).deleteAllValues(list(3), list(4, 5)));
        
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).DeleteAllValues(l(3), l(4, 5)).m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).deleteAllValues(l(3), l(4, 5)));
    }
    
    @Test
    public void testGet() {
        assertEquals(0, m("a", 0).i("b", 1).i("c", 2).get("a"));
        assertEquals(list(0, 2), m("a", 0).i("b", 1).i("c", 2).get("a", "c"));
        assertEquals(list(0, 2), m("a", 0).i("b", 1).i("c", 2).Get("a", "c").l);
        assertEquals(list(0, 2), m("a", 0).i("b", 1).i("c", 2).g("a", "c").l);
        
        // get all
        assertEquals(EXPECTED_LIST, m("a", 0).i("b", 1).i("c", 2).getValues());
        assertEquals(set(0, 1, 2), m(0, "a").i(1, "b").i(2, "c").getKeys());
        assertEquals(set(e(0, "a"), e(1, "b"), e(2, "c")), m(0, "a").i(1, "b").i(2, "c").getEntries());
        
        assertEquals(0, m("a", 0).i("b", 1).i("c", 2).getOrDefault("a", 3));
        assertEquals(3, m("a", 0).i("b", 1).i("c", 2).getOrDefault("d", 3));
        
        assertEquals(true, m("a", 0).i("b", 1).i("c", 2).containsAnyValue(0));
        assertEquals(true, m("a", 0).i("b", 1).i("c", 2).containsAnyValue(0, 3));
        assertEquals(false, m("a", 0).i("b", 1).i("c", 2).containsAnyValue(3));
        assertEquals(false, m("a", 0).i("b", 1).i("c", 2).containsAnyValue(3, 4));
        assertEquals(true, m("a", 0).i("b", 1).i("c", 2).containsAnyKey("a"));
        assertEquals(true, m("a", 0).i("b", 1).i("c", 2).containsAnyKey("a", "d"));
        assertEquals(false, m("a", 0).i("b", 1).i("c", 2).containsAnyKey("d"));
        assertEquals(false, m("a", 0).i("b", 1).i("c", 2).containsAnyKey("d", "e"));
        
        assertEquals(true, m("a", 0).i("b", 1).i("c", 2).containsAnyValue(list(0), list(3)));
        assertEquals(false, m("a", 0).i("b", 1).i("c", 2).containsAnyValue(list(3), list(4)));
        assertEquals(true, m("a", 0).i("b", 1).i("c", 2).containsAnyKey(list("a"), list("d")));
        assertEquals(false, m("a", 0).i("b", 1).i("c", 2).containsAnyKey(list("d"), list("e")));
        
        assertEquals(0, m(0, "a").i(1, "b").i(2, "c").indexOf("a"));
        assertEquals(0, m(0, "a").i(1, "b").i(2, "c").getKey("a"));
        assertEquals(null, m(0, "a").i(1, "b").i(2, "c").indexOf("d"));
        assertEquals(null, m(0, "a").i(1, "b").i(2, "c").getKey("d"));
    }
}

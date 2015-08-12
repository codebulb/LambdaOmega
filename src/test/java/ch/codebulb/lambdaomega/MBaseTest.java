package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.*;
import static ch.codebulb.lambdaomega.M.*;
import static ch.codebulb.lambdaomega.TestUtil.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

// TODO Restructure tests to include Map-as-SequentialI tests
public class MBaseTest {
    @Test
    public void testBasics() {
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
    public void testConvert() {
        
    }
    
    @Test
    public void testCC() {
        assertEquals(0, m().size());
        assertEquals(3, m("a", 0).i("b", 1).i("c", 2).size());
        
        assertTrue(m().isEmpty());
        assertFalse(m("a", 0).i("b", 1).i("c", 2).isEmpty());
    }
    
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
        catch (M.MapEntryKeyAlreadyPresentException ex) {
            assertEquals("b", ex.key);
            assertEquals(1, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.insert("b", 2);
            fail();
        }
        catch (M.MapEntryKeyAlreadyPresentException ex)  {
            assertEquals("b", ex.key);
            assertEquals(1, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.i("b", 2);
            fail();
        }
        catch (M.MapEntryKeyAlreadyPresentException ex)  {
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
        catch (M.MapEntryKeyAlreadyPresentException ex) {
            assertEquals("b", ex.key);
            assertEquals(1, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.insertAll(m("b", 4).i("c", 2).m, m("a", 3).m);
            fail();
        }
        catch (M.MapEntryKeyAlreadyPresentException ex) {
            assertEquals("b", ex.key);
            assertEquals(1, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.I(m("b", 4).i("c", 2).m, m("a", 3).m);
            fail();
        }
        catch (M.MapEntryKeyAlreadyPresentException ex) {
            assertEquals("b", ex.key);
            assertEquals(1, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.InsertAll(m("c", 2).i("d", 3).m, m("d", 4).m);
            fail();
        }
        catch (M.MapEntryKeyAlreadyPresentException ex) {
            assertEquals("d", ex.key);
            assertEquals(3, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.insertAll(m("c", 2).i("d", 3), m("d", 4));
            fail();
        }
        catch (M.MapEntryKeyAlreadyPresentException ex) {
            assertEquals("d", ex.key);
            assertEquals(3, ex.previousValue);
        }
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.I(m("c", 2).i("d", 3), m("d", 4));
            fail();
        }
        catch (M.MapEntryKeyAlreadyPresentException ex) {
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
        catch (M.MapEntryKeyAlreadyPresentException ex) {}
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.insertAll(m("b", 1).i("c", 2), m("a", 3));
            fail();
        }
        catch (M.MapEntryKeyAlreadyPresentException ex) {}
        assertEquals(EXPECTED_MAP_2_ELEMENTS, addMap.m);
        addMap = m("a", 0).i("b", 1);
        try {
            addMap.I(m("b", 1).i("c", 2), m("a", 3));
            fail();
        }
        catch (M.MapEntryKeyAlreadyPresentException ex) {}
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
        assertEquals(EXPECTED_MAP, m("a", 0).PutAll(m("b", 1), m("c", 2)).m);
        assertEquals(EXPECTED_MAP, m("a", 0).putAll(m("b", 1).m, m("c", 2).m));
        assertEquals(EXPECTED_MAP, m("a", 0).putAll(m("b", 1), m("c", 2)));
        
        // Set / replace
        M<String, Number> replaceMap = m(String.class, Number.class).i("a", 3).i("b", 1).i("c", 2);
        assertEquals(EXPECTED_MAP, replaceMap.set("a", 0), replaceMap.m);
        assertEquals(EXPECTED_MAP, m("a", 3).i("b", 1).i("c", 2).Set("a", 0).m);
        assertEquals(EXPECTED_MAP, m("a", 3).i("b", 1).i("c", 2).s("a", 0).m);
        
        assertEquals(EXPECTED_MAP, m("a", 0).SetAll(m("b", 1).m, m("c", 2).m).m);
        assertEquals(EXPECTED_MAP, m("a", 0).SetAll(m("b", 1), m("c", 2)).m);
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
        // Remove individual elements
        assertEquals(EXPECTED_MAP, m("d", 3).i("a", 0).i("b", 1).i("c", 2).DeleteKey("d").m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("a", 0).i("b", 1).i("c", 2).d("d").m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("a", 0).i("b", 1).i("c", 2).deleteKey("d"));
        assertEquals(EXPECTED_MAP, m("d", 3).i("a", 0).i("b", 1).i("c", 2).deleteValue(3));
        assertEquals(EXPECTED_MAP, m("a", 0).i("b", 1).i("c", 2).deleteValue(-1));
        
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("a", 0).i("b", 1).i("c", 2).DeleteKey("d", "e").m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("a", 0).i("b", 1).i("c", 2).d("d", "e").m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("a", 0).i("b", 1).i("c", 2).deleteKey("d", "e"));
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("a", 0).i("b", 1).i("c", 2).deleteValue(3, 4));
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 3).i("a", 0).i("b", 1).i("c", 2).deleteValue(3, 3));
        
        // Remove individual elements at index
        assertEquals(EXPECTED_MAP, m("d", 3).i("a", 0).i("b", 1).i("c", 2).deleteKey("d"));
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("a", 0).i("b", 1).i("c", 2).deleteKey("d", "e"));
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("a", 0).i("b", 1).i("c", 2).deleteKey("e", "d"));
        
        // Remove all entries
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).DeleteAllKeys(list("d"), list("e", "f")).m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).deleteAllKeys(list("d"), list("e", "f")));
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).DeleteAllValues(list(3), list(4, 5)).m);
        assertEquals(EXPECTED_MAP, m("d", 3).i("e", 4).i("f", 5).i("a", 0).i("b", 1).i("c", 2).deleteAllValues(list(3), list(4, 5)));
        
        M<String, Number> removeMap = m(String.class, Number.class).i("d", 3).i("a", 0).i("b", 1).i("c", 2);
        Map<String, Number> removeMapExpected = m(String.class, Number.class).i("d", 3).i("a", 0).i("b", 1).i("c", 2).m;
        assertEquals(true, removeMap.deleteIfMatches("d", 3));
        assertEquals(true, removeMapExpected.remove("d", 3));
        assertEquals(EXPECTED_MAP, removeMap.m);
        assertEquals(false, removeMap.deleteIfMatches("a", 4));
        assertEquals(false, removeMapExpected.remove("a", 4));
        assertEquals(EXPECTED_MAP, removeMapExpected);
    }
    
    @Test
    public void testGet() {
        assertEquals(0, m("a", 0).i("b", 1).i("c", 2).get("a"));
        assertEquals(0, m("a", 0).i("b", 1).i("c", 2).getOrDefault("a", 3));
        assertEquals(3, m("a", 0).i("b", 1).i("c", 2).getOrDefault("d", 3));
        
        assertEquals(true, m("a", 0).i("b", 1).i("c", 2).containsValue(0));
        assertEquals(false, m("a", 0).i("b", 1).i("c", 2).containsValue(3));
        assertEquals(true, m("a", 0).i("b", 1).i("c", 2).containsKey("a"));
        assertEquals(false, m("a", 0).i("b", 1).i("c", 2).containsKey("d"));
    }
    
    @Test
    public void testWithDefault() {
        M<String, List<String>> mapWithDefault = m("a", list("a?")).i("b", list("b?")).i("c", list("c?")).WithDefault(it -> list(it + '!'));
        assertEquals(list("a?"), mapWithDefault.get("a"));
        assertEquals(list("e!"), mapWithDefault.get("e"));
        
        mapWithDefault.get("a").add("y");
        assertEquals(list("a?", "y"), mapWithDefault.get("a"));
        mapWithDefault.get("e").add("z");
        // original entry has been changed
        assertEquals(list("e!", "z"), mapWithDefault.get("e"));
    }
    
    @Test
    public void testMapAccess() {
        assertEquals(EXPECTED_LIST, m("a", 0).i("b", 1).i("c", 2).getValues());
        assertEquals(EXPECTED_LIST, new ArrayList<>(m(0, "a").i(1, "b").i(2, "c").getKeys()));
    }
}

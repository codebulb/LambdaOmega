package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.l;
import static ch.codebulb.lambdaomega.L.list;
import static ch.codebulb.lambdaomega.M.e;
import static ch.codebulb.lambdaomega.M.m;
import static ch.codebulb.lambdaomega.S.set;
import static ch.codebulb.lambdaomega.TestUtil.EXPECTED_MAP;
import static ch.codebulb.lambdaomega.TestUtil.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test cases for the {@link M} implementation for {@link SequentialI}.
 */
public class MBaseSequentialTest {
    @Test
    public void testConvert() {
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), m("a", 0).i("b", 1).i("c", 2).toSet());
        
        assertEquals(m("a", 0).i("b", 1).i("c", 2).m, m("a", 0).i("b", 1).i("c", 2).toMap());
    }
    
    @Test
    public void testCollection() {
        assertEquals(0, m().size());
        assertEquals(3, m("a", 0).i("b", 1).i("c", 2).size());
        
        assertTrue(m().isEmpty());
        assertFalse(m("a", 0).i("b", 1).i("c", 2).isEmpty());
        
        assertEquals(set(), m("a", 0).i("b", 1).i("c", 2).clear());
    }
    
    @Test
    public void testAdd() {
        // Add individual elements
        assertEquals(EXPECTED_MAP, m("a", 0).i("b", 1).Add(e("c", 2)).m);
        assertEquals(EXPECTED_MAP, m("a", 0).i("b", 1).a(e("c", 2)).m);
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), m("a", 0).i("b", 1).add(e("c", 2)));
        
        assertEquals(EXPECTED_MAP, m(Number.class).Add(e("a", 0), e("b", 1), e("c", 2)).m);
        assertEquals(EXPECTED_MAP, m(Number.class).a(e("a", 0), e("b", 1), e("c", 2)).m);
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), m(Number.class).add(e("a", 0), e("b", 1), e("c", 2)));
        
        // Add all entries
        assertEquals(EXPECTED_MAP, m(Number.class).AddAll(list(e("a", 0), e("b", 1), e("c", 2))).m);
        assertEquals(EXPECTED_MAP, m(Number.class).A(list(e("a", 0), e("b", 1), e("c", 2))).m);
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), m(Number.class).addAll(list(e("a", 0), e("b", 1), e("c", 2))));
        
        assertEquals(EXPECTED_MAP, m(Number.class).AddAll(list(e("a", 0), e("b", 1)), list(e("c", 2))).m);
        assertEquals(EXPECTED_MAP, m(Number.class).A(list(e("a", 0), e("b", 1)), list(e("c", 2))).m);
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), m(Number.class).addAll(list(e("a", 0), e("b", 1)), list(e("c", 2))));
        
        // Add all L entries
        assertEquals(EXPECTED_MAP, m(Number.class).AddAll(l(e("a", 0), e("b", 1), e("c", 2))).m);
        assertEquals(EXPECTED_MAP, m(Number.class).A(l(e("a", 0), e("b", 1), e("c", 2))).m);
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), m(Number.class).addAll(l(e("a", 0), e("b", 1), e("c", 2))));
        
        assertEquals(EXPECTED_MAP, m(Number.class).AddAll(l(e("a", 0), e("b", 1)), l(e("c", 2))).m);
        assertEquals(EXPECTED_MAP, m(Number.class).A(l(e("a", 0), e("b", 1)), l(e("c", 2))).m);
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), m(Number.class).addAll(l(e("a", 0), e("b", 1)), l(e("c", 2))));
    }
    
    @Test
    public void testRemove() {
        // Remove individual entries
        assertEquals(EXPECTED_MAP, m("a", 0).i("b", 1).i("c", 2).i("d", 2).Remove(e("d", 2)).m);
        assertEquals(EXPECTED_MAP, m("a", 0).i("b", 1).i("c", 2).i("d", 2).r(e("d", 2)).m);
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), m("a", 0).i("b", 1).i("c", 2).i("d", 2).remove(e("d", 2)));
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), m("a", 0).i("b", 1).i("c", 2).remove(e("c", 3)));
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), m("a", 0).i("b", 1).i("c", 2).remove(e("d", 2)));
        
        
        // Remove all entries
        M<String, Integer> removeMap = m("a", 0).i("b", 1).i("c", 2).i("d", 2).i("e", 2).i("f", 2);
        assertEquals(EXPECTED_MAP, removeMap.RemoveAll(list(e("d", 2)), list(e("e", 2), e("f", 2))).m);
        removeMap = m("a", 0).i("b", 1).i("c", 2).i("d", 2).i("e", 2).i("f", 2);
        assertEquals(EXPECTED_MAP, removeMap.R(list(e("d", 2)), list(e("e", 2), e("f", 2))).m);
        removeMap = m("a", 0).i("b", 1).i("c", 2).i("d", 2).i("e", 2).i("f", 2);
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), removeMap.removeAll(list(e("d", 2)), list(e("e", 2), e("f", 2))));
        
        removeMap = m("a", 0).i("b", 1).i("c", 2).i("d", 2).i("e", 2).i("f", 2);
        assertEquals(EXPECTED_MAP, removeMap.RemoveAll(l(e("d", 2)), l(e("e", 2), e("f", 2))).m);
        removeMap = m("a", 0).i("b", 1).i("c", 2).i("d", 2).i("e", 2).i("f", 2);
        assertEquals(EXPECTED_MAP, removeMap.R(l(e("d", 2)), l(e("e", 2), e("f", 2))).m);
        removeMap = m("a", 0).i("b", 1).i("c", 2).i("d", 2).i("e", 2).i("f", 2);
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), removeMap.removeAll(l(e("d", 2)), l(e("e", 2), e("f", 2))));
        
        removeMap = m("a", 0).i("b", 1).i("c", 2).i("d", 2).i("e", 2).i("f", 2);
        assertEquals(set(e("a", 0), e("b", 1), e("c", 2)), removeMap.retainAll(list(e("a", 0)), list(e("b", 1), e("c", 2))));
    }
    
    @Test
    public void testGet() {
        assertEquals(true, m("a", 0).i("b", 1).i("c", 2).contains(e("a", 0)));
        assertEquals(true, m("a", 0).i("b", 1).i("c", 2).contains(e("a", 0), e("c", 2)));
        assertEquals(false, m("a", 0).i("b", 1).i("c", 2).contains(e("a", 0), e("d", 2)));
        
        assertEquals(true, m("a", 0).i("b", 1).i("c", 2).containsAll(list(e("a", 0), e("b", 1)), list(e("c", 2))));
        assertEquals(false, m("a", 0).i("b", 1).i("c", 2).containsAll(list(e("a", 0), e("b", 1)), list(e("c", 2), e("d", 2))));
    }
}

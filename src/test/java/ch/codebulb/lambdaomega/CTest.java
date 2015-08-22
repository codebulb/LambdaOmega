package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.*;
import static ch.codebulb.lambdaomega.M.m;
import static ch.codebulb.lambdaomega.TestUtil.assertEquals;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.PriorityBlockingQueue;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

public class CTest {
    @Test
    public void testConvert() {
        testConvert(new ArrayBlockingQueue<>(3));
        testConvert(new ArrayDeque<>());
        testConvert(new ArrayList<>());
        testConvert(new ConcurrentLinkedDeque<>());
        testConvert(new ConcurrentLinkedQueue<>());
        testConvert(new ConcurrentSkipListSet<>());
        testConvert(new CopyOnWriteArrayList<>());
        testConvert(new CopyOnWriteArraySet<>());
        testConvert(new HashSet<>());
        testConvert(new LinkedBlockingDeque<>());
        testConvert(new LinkedBlockingQueue<>());
        testConvert(new LinkedHashSet<>());
        testConvert(new LinkedList<>());
        testConvert(new LinkedTransferQueue<>());
        testConvert(new PriorityBlockingQueue<>());
        testConvert(new PriorityQueue<>());
        testConvert(new Stack<>());
        testConvert(new TreeSet<>());
        
        testConvert(new ConcurrentHashMap<>());
        testConvert(new ConcurrentSkipListMap<>());
        
        testConvert(new HashMap<>());
        testConvert(new Hashtable<>());
        testConvert(new IdentityHashMap<>());
        testConvert(new LinkedHashMap<>());
        testConvert(new Properties());
        testConvert(new TreeMap<>());
        testConvert(new WeakHashMap<>());
        
        assertEquals(list(1, 2, 3), C.map(new Integer[]{0, 1, 2}, it -> it + 1));
        assertEquals(list(1, 2, 3), C.map(list(0, 1, 2), it -> it + 1));
    }
    
    private static void testConvert(Collection<Integer> expected) {
        expected.add(0);
        expected.add(1);
        expected.add(2);
        Collection convertedL = l(0, 1, 2).to(expected.getClass());
        assertEquals(expected.getClass(), convertedL.getClass());
        // use array for simpler assertion check
        assertArrayEquals(expected.toArray(), convertedL.toArray());
    }
    
    private static void testConvert(Map expected) {
        expected.put("a", 0);
        expected.put("b", 1);
        expected.put("c", 2);
        Map convertedL = m("a", 0).i("b", 1).i("c", 2).to(expected.getClass());
        assertEquals(expected.getClass(), convertedL.getClass());
        // use array for simpler assertion check
        assertEquals(expected, convertedL);
    }
}

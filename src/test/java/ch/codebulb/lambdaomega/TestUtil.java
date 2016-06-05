package ch.codebulb.lambdaomega;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;

class TestUtil {
    public static final List<Integer> EXPECTED_LIST = new ArrayList<>();
    static {
        EXPECTED_LIST.add(0);
        EXPECTED_LIST.add(1);
        EXPECTED_LIST.add(2);
    }
    
    public static final List<List<Integer>> EXPECTED_NESTED_LIST = new ArrayList<>();
    static {
        EXPECTED_NESTED_LIST.add(EXPECTED_LIST);
        EXPECTED_NESTED_LIST.add(EXPECTED_LIST);
    }
    
    public static final Map<String, Integer> EXPECTED_MAP = new LinkedHashMap<>();
    static {
        EXPECTED_MAP.put("a", 0);
        EXPECTED_MAP.put("b", 1);
        EXPECTED_MAP.put("c", 2);
    }
    
    public static final Map<String, Integer> EXPECTED_MAP_2_ELEMENTS = new LinkedHashMap<>();
    static {
        EXPECTED_MAP_2_ELEMENTS.put("a", 0);
        EXPECTED_MAP_2_ELEMENTS.put("b", 1);
    }
    
    public static final Set<Integer> EXPECTED_SET = new LinkedHashSet<>();
    static {
        EXPECTED_SET.add(0);
        EXPECTED_SET.add(1);
        EXPECTED_SET.add(2);
    }
    
    public static final Set<Set<Integer>> EXPECTED_NESTED_SET = new LinkedHashSet<>();
    static {
        EXPECTED_NESTED_SET.add(EXPECTED_SET);
        
        Set<Integer> set2 = new LinkedHashSet<>();
        set2.add(3);
        set2.add(4);
        set2.add(5);
        EXPECTED_NESTED_SET.add(set2);
    }
    
    public static <T> void assertEquals(T expected, T... actual) {
        Arrays.stream(actual).forEach(it -> Assert.assertEquals(expected, it));
    }
}

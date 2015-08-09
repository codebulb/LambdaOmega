package ch.codebulb.lambdaomega;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    
    public static <T> void assertEquals(T expected, T... actual) {
        Arrays.stream(actual).forEach(it -> Assert.assertEquals(expected, it));
    }
}

package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.M.*;
import static ch.codebulb.lambdaomega.TestUtil.*;
import java.util.Map;
import org.junit.Test;

public class MFunctionsTest {
    private final Map<String, Integer> actual = map(String.class, Integer.class);
    private final M<String, Integer> original = m().i("a", 0).i("b", 1).i("c", 2);
    
    @Test
    public void testForEach() {
        original.forEach((k, v) -> actual.put(k, v));
        assertEquals(EXPECTED_MAP, actual);
    }
}

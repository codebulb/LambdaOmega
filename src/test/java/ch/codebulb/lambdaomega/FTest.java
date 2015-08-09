package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.F.f;
import static ch.codebulb.lambdaomega.L.*;
import static ch.codebulb.lambdaomega.TestUtil.assertEquals;
import org.junit.Test;

public class FTest {
    @Test
    public void testBasics() {
        assertEquals(1, f((Integer it) -> it + 1).call(0));
        assertEquals(1, f((Integer it) -> it + 1).get(0));
        assertEquals(list(1, 2, 3), f((Integer it) -> it + 1).get(0, 1, 2));
        assertEquals(l(1, 2, 3), f((Integer it) -> it + 1).Get(0, 1, 2));
        assertEquals(l(1, 2, 3), f((Integer it) -> it + 1).g(0, 1, 2));
    }
}

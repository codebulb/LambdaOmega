package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.*;
import static ch.codebulb.lambdaomega.TestUtil.assertEquals;
import org.junit.Test;

public class CTest {
    @Test
    public void testConvert() {
        assertEquals(list(1, 2, 3), C.map(new Integer[]{0, 1, 2}, it -> it + 1));
        assertEquals(list(1, 2, 3), C.map(list(0, 1, 2), it -> it + 1));
    }
}

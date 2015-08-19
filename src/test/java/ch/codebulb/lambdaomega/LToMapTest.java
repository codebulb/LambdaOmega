package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.l;
import static ch.codebulb.lambdaomega.M.m;
import static ch.codebulb.lambdaomega.TestUtil.assertEquals;
import org.junit.Test;

// Separated from other tests, because here, we want to explicitly call #toMap()
public class LToMapTest {
    @Test
    public void testToMap() {
        assertEquals(m(0, "a").i(1, "b").i(2, "c").m, l("a", "b", "c").toMap());
    }
}

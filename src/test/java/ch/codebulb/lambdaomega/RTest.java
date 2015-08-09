package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.*;
import static ch.codebulb.lambdaomega.R.r;
import static ch.codebulb.lambdaomega.TestUtil.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

public class RTest {
    public static final List<Integer> EXPECTED_RANGE_LIST = IntStream.range(0, 3).boxed().collect(Collectors.toList());
    public static final List<Integer> EXPECTED_RANGE_LIST_CLOSED = IntStream.rangeClosed(0, 2).boxed().collect(Collectors.toList());
    
    @Test
    public void test() {
        assertEquals(list(0, 1, 2), EXPECTED_RANGE_LIST, r(0).with(2).list);
        assertEquals(list(0, 1, 2), EXPECTED_RANGE_LIST_CLOSED, r(0).to(3).list);
        
        assertEquals(0, r(0).with(2).startInclusive);
        assertEquals(2, r(0).with(2).endExclusive);
        assertEquals(2, r(0).to(3).endExclusive);
    }
}

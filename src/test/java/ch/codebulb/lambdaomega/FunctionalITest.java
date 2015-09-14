package ch.codebulb.lambdaomega;

import ch.codebulb.lambdaomega.abstractions.FunctionalI;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FunctionalITest {
    @Test
    public void testNegate() {
        FunctionalI<Integer, Boolean> predicateFunctional = x -> x < 10;
        assertTrue(predicateFunctional.call(9));
        assertFalse(predicateFunctional.call(10));
        assertFalse(predicateFunctional.call(11));
        FunctionalI<Integer, Boolean> predicateFunctionalNegated = predicateFunctional.negate();
        assertFalse(predicateFunctionalNegated.call(9));
        assertTrue(predicateFunctionalNegated.call(10));
        assertTrue(predicateFunctionalNegated.call(11));
    }
    
    // Other methods are tested in FTest
}
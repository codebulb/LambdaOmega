package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.TestUtil.*;
import static ch.codebulb.lambdaomega.U.Choose;
import static org.junit.Assert.fail;
import org.junit.Test;

public class UTest {
    private int actual;
    @Test
    public void testChoose() {
        // With return value
        actual = Choose(false, () -> 1).Or(true, () -> 2).Or(true, () -> 3).orNull();
        assertEquals(2, actual);
        
        actual = Choose(false, () -> 1).Or(false, () -> 2).Or(false, () -> 3).or(0);
        assertEquals(0, actual);
        
        try {
            actual = Choose(false, () -> 1).Or(false, () -> 2).Or(false, () -> 3).orThrow();
            fail();
        } catch (U.Choice.NoValidChoiceException ex) {
            // expected
        } catch (Exception ex) {
            fail();
        }
        
        // With side effect
        actual = 0;
        Choose(false, () -> actual = 1).Or(true, () -> actual = 2).Or(true, () -> actual = 3).orNull();
        assertEquals(2, actual);
        
        actual = 0;
        Choose(false, () -> actual = 1).Or(false, () -> actual = 2).Or(false, () -> actual = 3).orNull();
        assertEquals(0, actual);
    }
}

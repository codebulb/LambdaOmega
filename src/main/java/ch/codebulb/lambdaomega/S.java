package ch.codebulb.lambdaomega;

import ch.codebulb.lambdaomega.abstractions.OmegaObject;

import java.util.Set;

/**
 * The "S" stands for "set". This implementation is incomplete.
 */
// TODO Complete implementation
public class S extends OmegaObject {
    private S() {}
    
    /**
     * Creates a {@link Set} consisting of the entries provided.
     */
    public static <T> Set<T> set(T... ts) {
        return C.toSet(ts);
    }
}

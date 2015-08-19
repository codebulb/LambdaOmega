package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.U;

/**
 * Base class for all instantiable classes. Provides miscellaneous instance util functionality. 
 * It's a design goal to keep this class very slim.
 */
public class OmegaObject {
    public void println() {
        U.println(this);
    }
}

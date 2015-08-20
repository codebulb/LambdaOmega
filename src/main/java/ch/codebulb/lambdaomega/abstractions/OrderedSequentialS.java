package ch.codebulb.lambdaomega.abstractions;

import ch.codebulb.lambdaomega.L;
import static ch.codebulb.lambdaomega.L.L;
import ch.codebulb.lambdaomega.R;

/**
 * Adds syntactic sugar to a {@link OrderedSequential}.
 */
public interface OrderedSequentialS<T> extends OrderedSequential<T> {
    /**
     * @see #get(R)
     */
    public default L<T> Get(R range) {
        return L(get(range));
    }
    
    /**
     * @see #get(R)
     */
    public default L<T> g(R range) {
        return Get(range);
    }
}

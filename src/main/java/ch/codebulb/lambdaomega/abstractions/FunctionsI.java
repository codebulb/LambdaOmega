package ch.codebulb.lambdaomega.abstractions;

import java.util.Collection;
import java.util.stream.Collector;

/**
 * A common interface for functional operations on any {@link Collection} type.
 */
public interface FunctionsI {
    /**
     * Returns a collector to collect results in the type expected by the interface implementation.<p>
     * 
     * Note: When you implement this function, you have to make sure that every method which uses this method
     * to create its return value is overridden to match the overriden return type.
     */
    <R> Collector<R, ?, ? extends Collection<R>> createCollector();
}

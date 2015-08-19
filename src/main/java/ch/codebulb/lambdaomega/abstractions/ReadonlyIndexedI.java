package ch.codebulb.lambdaomega.abstractions;

import java.util.Map;

/**
 * An abstraction for a data structure with indexed (random) read-only access. This interface makes no assumptions about the data structure's ordering.
 * It is loosely inspired by read-only methods of {@link Map}.
 */
public interface ReadonlyIndexedI<K, V> extends I<K, V> {
    
}

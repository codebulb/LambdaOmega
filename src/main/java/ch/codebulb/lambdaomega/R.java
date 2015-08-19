package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.*;
import ch.codebulb.lambdaomega.abstractions.OmegaObject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The "R" stands for "range". An implementation of a wrapper API for an {@link IntStream} range. This is an immutable type.
 */
public class R extends OmegaObject {
    public final int startInclusive;
    public final int endExclusive;
    public final IntStream r;
    public final L<Integer> l;
    public final List<Integer> list;
    
    public R(int startInclusive, int end, boolean endInclusive) {
        this.startInclusive = startInclusive;
        this.endExclusive = endInclusive ? end : end - 1;
        r = createStream(startInclusive, end, endInclusive);
        // don't reuse a stream, create a new one!
        list = createStream(startInclusive, end, endInclusive).boxed().collect(Collectors.toList());
        l = L(list);
    }
    
    private static IntStream createStream(int startInclusive, int end, boolean endInclusive) {
        return endInclusive ? IntStream.rangeClosed(startInclusive, end) : 
                IntStream.range(startInclusive, end);
    }
    
    public static Rinit r(int startInclusive) {
        return new Rinit(startInclusive);
    }
    
    public static class Rinit {
        private final int startInclusive;

        public Rinit(int startInclusive) {
            this.startInclusive = startInclusive;
        }
        
        public R end(int end, boolean endInclusive) {
            return new R(startInclusive, end, endInclusive);
        }
        
        public R to(int endExclusive) {
            return end(endExclusive, false);
        }
        
        public R excl(int endExclusive) {
            return to(endExclusive);
        }
        
        public R with(int endInclusive) {
            return end(endInclusive, true);
        }
        
        public R incl(int endInclusive) {
            return with(endInclusive);
        }
    }
    
    public L<Integer> toL() {
        return l;
    }
        
    public List<Integer> toList() {
        return list;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.startInclusive;
        hash = 29 * hash + this.endExclusive;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final R other = (R) obj;
        if (this.startInclusive != other.startInclusive) {
            return false;
        }
        if (this.endExclusive != other.endExclusive) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + startInclusive + "..<" + endExclusive + "]";
    }
    
    
}

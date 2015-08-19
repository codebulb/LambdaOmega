package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.l;
import static ch.codebulb.lambdaomega.V2.v;
import java.util.function.Supplier;

/**
 * The "U" stands for "utils". Provides common static helper methods.
 *
 */
public class U {
    private U() {}
    
    public static void println(Object out) {
        System.out.println(out);
    }
    
    public static <T> Choice<T> Choose(boolean predicate, Supplier<T> function) {
        return new Choice<>(predicate, function);
    }
    
    /**
     * Represents a simple choice tree. This structure can be used as an alternative to
     * an <code>if</code> / <code>else if</code> / <code>else</code> structure with optionally enforced choice.
     *
     * @param <T> the return type
     */
    public static class Choice<T> {
        private final L<V2<Boolean, Supplier<T>>> choices;
        
        public Choice(boolean predicate, Supplier<T> function) {
            choices = l(v(predicate, function));
        }
        
        public Choice<T> Or(boolean predicate, Supplier<T> function) {
            choices.a(v(predicate, function));
            return this;
        }
        
        public T or(T defaultValue) {
            V2<Boolean, Supplier<T>> choice = choices.find(it -> it.get0() == true);
            return choice != null ? choice.get1().get() : defaultValue;
        }
        
        public T orNull() {
            return or(null);
        }
        
        public T orThrow() throws NoValidChoiceException {
            V2<Boolean, Supplier<T>> choice = choices.find(it -> it.get0() == true);
            if (choice != null) {
                return choice.get1().get();
            }
            else {
                throw new NoValidChoiceException();
            }
        }
        
        /**
         * Signals that none of the {@link Choice}s provided resolved to <code>true</code>.
         */
        public static class NoValidChoiceException extends Exception {
            
        }
    }
}

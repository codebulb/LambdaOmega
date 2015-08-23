package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.l;
import static ch.codebulb.lambdaomega.V2.v;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * The "U" stands for "utils". Provides common static helper methods.
 */
public class U {
    private U() {}
    
    /**
     * Prints the object provided to the standard output.
     */
    public static void println(Object out) {
        System.out.println(out);
    }
    
    /**
     * Creates a {@link Choice}.
     */
    public static <T> Choice<T> Choose(boolean predicate, Supplier<T> function) {
        return new Choice<>(predicate, function);
    }
    
    /**
     * Returns <code>true</code> if the <code>predicate</code> provided is <code>true</code> for at least one of the <code>candidates</code>.
     */
    public static <T> boolean any(Predicate<T> predicate, T... candidates) {
        return C.toStream(candidates).anyMatch(predicate);
    }
    
    /**
     * Represents a simple choice tree. This structure can be used as an alternative to
     * an <code>if</code> / <code>else if</code> / <code>else</code> structure with optionally enforced choice.<p/>
     * 
     * The constructor of this class is not visible; use the convenience {@link U#Choose(boolean, Supplier)} method to create a new instance of this class.
     * It's best practice to statically import this function in client code.
     *
     * @param <T> the return type
     */
    public static class Choice<T> {
        private final L<V2<Boolean, Supplier<T>>> choices;
        
        Choice(boolean predicate, Supplier<T> function) {
            choices = l(v(predicate, function));
        }
        
        /**
         * Adds an "or" joined option.
         */
        public Choice<T> Or(boolean predicate, Supplier<T> function) {
            choices.a(v(predicate, function));
            return this;
        }
        
        /**
         * Returns the result of the first "or" option which evaluates to <code>true</code> or the default value provided if there is no valid option.
         */
        public T or(T defaultValue) {
            V2<Boolean, Supplier<T>> choice = choices.find(it -> it.get0() == true);
            return choice != null ? choice.get1().get() : defaultValue;
        }
        
        /**
         * Returns the result of the first "or" option which evaluates to <code>true</code> or <code>null</code> if there is no valid option.
         */
        public T orNull() {
            return or(null);
        }
        
        /**
         * Returns the result of the first "or" option which evaluates to <code>true</code> or throws a {@link NoValidChoiceException} if there is no valid option.
         */
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

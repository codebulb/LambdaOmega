/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.codebulb.lambdaomega.abstractions.functions;

/**
 * Represents a function <i>f: (<code>long</code>, <code>long</code>) &rarr; <code>R</code></i> that accepts  two {@code long}-valued operands and produces a
 * result.  This is the {@code long}-consuming primitive specialization for
 * {@link BiFunction}.
 *
 * <p>This is a functional interface
 * whose functional method is {@link #apply(long, long)}.
 *
 * @param <R> the type of the result of the function
 *
 * @see BiFunction
 */
@FunctionalInterface
public interface LongBiFunction<R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(long t, long u);
    
}

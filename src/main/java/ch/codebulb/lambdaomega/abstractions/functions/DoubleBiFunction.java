/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.codebulb.lambdaomega.abstractions.functions;

/**
 * Represents a function <i>f: (<code>double</code>, <code>double</code>) &rarr; <code>R</code></i> that accepts  two {@code double}-valued operands and produces a
 * result.  This is the {@code double}-consuming primitive specialization for
 * {@link BiFunction}.
 *
 * <p>This is a functional interface
 * whose functional method is {@link #apply(double, double)}.
 *
 * @param <R> the type of the result of the function
 *
 * @see BiFunction
 */
@FunctionalInterface
public interface DoubleBiFunction<R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(double t, double u);
    
}

package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.l;
import static ch.codebulb.lambdaomega.U.*;
import ch.codebulb.lambdaomega.abstractions.OmegaObject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A drop-in-replacement for {@link CompletableFuture} with a simplified, more concise API. It wraps around a {@link CompletableFuture}.
 */
public class Promise<T> extends OmegaObject {
    public final CompletableFuture<T> wrapped;
    
    public static <T> Promise<T> wrap(CompletableFuture<T> inner) {
        return new Promise<>(inner);
    }
    
    /**
     * @see CompletableFuture#CompletableFuture()
     */
    public Promise() {
        this(new CompletableFuture<>());
    }

    public Promise(CompletableFuture<T> inner) {
        this.wrapped = inner;
    }
    
    /**
     * @see CompletableFuture#supplyAsync(Supplier, Executor)
     */
    public static <U> Promise<U> completeAsync(Supplier<U> supplier, Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("Executor parameter must not be null.");
        }
        return new Promise<>(CompletableFuture.supplyAsync(supplier, executor));
    }
    
    /**
     * @see #completeAsync(Supplier, Executor)
     */
    public static <U> Promise<U> completeAsync(Supplier<U> supplier) {
        return new Promise<>(CompletableFuture.supplyAsync(supplier));
    }
    
    /**
     * @see CompletableFuture#runAsync(Runnable, Executor)
     */
    public static Promise<Void> completeAsync(Runnable runnable, Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("Executor parameter must not be null.");
        }
        return new Promise<>(CompletableFuture.runAsync(runnable, executor));
    }
    
    /**
     * @see #completeAsync(Runnable, Executor)
     */
    public static Promise<Void> completeAsync(Runnable runnable) {
        return new Promise<>(CompletableFuture.runAsync(runnable));
    }

    /**
     * @see CompletableFuture#thenApply(Function)
     */
    public <U> Promise<U> completed(Function<? super T, ? extends U> fn, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.thenApply(fn),
                () -> wrapped.thenApplyAsync(fn),
                () -> wrapped.thenApplyAsync(fn, executor));
    }
    
    /**
     * @see #completed(Function, boolean, Executor)
     */
    public <U> Promise<U> completed(Function<? super T, ? extends U> fn, boolean async) {
        return completed(fn, async, null);
    }
    
    /**
     * @see #completed(Function, boolean, Executor)
     */
    public <U> Promise<U> completed(Function<? super T, ? extends U> fn) {
        return completed(fn, false);
    }

    /**
     * @see CompletableFuture#thenAccept(Consumer)
     */
    public Promise<Void> completed(Consumer<? super T> action, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.thenAccept(action),
                () -> wrapped.thenAcceptAsync(action),
                () -> wrapped.thenAcceptAsync(action, executor));
    }
    
    /**
     * @see #completed(Consumer, boolean, Executor)
     */
    public Promise<Void> completed(Consumer<? super T> action, boolean async) {
        return completed(action, async, null);
    }
    
    /**
     * @see #completed(Consumer, boolean, Executor)
     */
    public Promise<Void> completed(Consumer<? super T> action) {
        return completed(action, false);
    }

    /**
     * @see CompletableFuture#thenRun(Runnable)
     */
    public Promise<Void> completed(Runnable action, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.thenRun(action),
                () -> wrapped.thenRunAsync(action),
                () -> wrapped.thenRunAsync(action, executor));
    }
    
    /**
     * @see #completed(Runnable, boolean, Executor)
     */
    public Promise<Void> completed(Runnable action, boolean async) {
        return completed(action, async, null);
    }
    
    /**
     * @see #completed(Runnable, boolean, Executor)
     */
    public Promise<Void> completed(Runnable action) {
        return completed(action, false);
    }

    /**
     * @see CompletableFuture#exceptionally(Function)
     */
    public Promise<T> completedExceptionally(Function<? extends Throwable, ? extends T> fn) {
        return wrap(wrapped.exceptionally((Function<Throwable, ? extends T>) fn));
    }

    /**
     * @see CompletableFuture#handle(BiFunction)
     */
    public <U> Promise<U> completed(BiFunction<? super T, Throwable, ? extends U> fn, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.handle(fn),
                () -> wrapped.handleAsync(fn),
                () -> wrapped.handleAsync(fn, executor));
    }
    
    /**
     * @see #completed(BiFunction, boolean, Executor)
     */
    public <U> Promise<U> completed(BiFunction<? super T, Throwable, ? extends U> fn, boolean async) {
        return completed(fn, async, null);
    }
    
    /**
     * @see #completed(BiFunction, boolean, Executor)
     */
    public <U> Promise<U> completed(BiFunction<? super T, Throwable, ? extends U> fn) {
        return completed(fn, false);
    }

    /**
     * @see CompletableFuture#whenComplete(BiConsumer)
     */
    public Promise<T> completed(BiConsumer<? super T, ? super Throwable> action, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.whenComplete(action),
                () -> wrapped.whenCompleteAsync(action),
                () -> wrapped.whenCompleteAsync(action, executor));
    }
    
    /**
     * @see #completed(BiConsumer, boolean, Executor)
     */
    public Promise<T> completed(BiConsumer<? super T, ? super Throwable> action, boolean async) {
        return completed(action, async, null);
    }
    
    /**
     * @see #completed(BiConsumer, boolean, Executor)
     */
    public Promise<T> completed(BiConsumer<? super T, ? super Throwable> action) {
        return completed(action, false);
    }

    /**
     * @see CompletableFuture#thenCompose(Function)
     */
    public <U> Promise<U> then(Function<? super T, ? extends Promise<U>> fn, boolean async, Executor executor) {
        Supplier<CompletableFuture<U>> syncS = () -> wrapped.thenCompose(f(fn));
        Supplier<CompletableFuture<U>> asyncS = () -> wrapped.thenComposeAsync(f(fn));
        Supplier<CompletableFuture<U>> asyncES = () -> wrapped.thenComposeAsync(f(fn), executor);
        
        return apply(async, executor, syncS, asyncS, asyncES);
    }
    
    private <U> Function<? super T, ? extends CompletionStage<U>> f(Function<? super T, ? extends Promise<U>> fn) {
        return new Function<T, CompletionStage<U>>() {
            @Override
            public CompletionStage<U> apply(T t) {
                return fn.apply(t).wrapped;
            }
        };
    }
    
    /**
     * @see #then(Function, boolean, Executor)
     */
    public <U> Promise<U> then(Function<? super T, ? extends Promise<U>> fn, boolean async) {
        return then(fn, async, null);
    }
    
    /**
     * @see #then(Function, boolean, Executor)
     */
    public <U> Promise<U> then(Function<? super T, ? extends Promise<U>> fn) {
        return then(fn, false);
    }
    
    /**
     * @see CompletableFuture#allOf(CompletableFuture...)
     */
    public static <T> Promise<List<T>> allOf(Promise<T>... cfs) {
        Promise<Void> allDoneFuture = new Promise<>(
                CompletableFuture.allOf(l(cfs).map(it -> it.wrapped)
                        .toArray(new CompletableFuture[cfs.length])
        ));
        return allDoneFuture.completed(it ->
            {return l(cfs).map(future -> future.wrapped.join());}
        );
    }

    /**
     * @see CompletableFuture#thenCombine(CompletionStage, BiFunction)
     */
    public <U, V> Promise<V> and(Promise<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.thenCombine(other.wrapped, fn),
                () -> wrapped.thenCombineAsync(other.wrapped, fn),
                () -> wrapped.thenCombineAsync(other.wrapped, fn, executor));
    }
    
    /**
     * @see #and(Promise, BiFunction, boolean, Executor)
     */
    public <U, V> Promise<V> and(Promise<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn, boolean async) {
        return and(other, fn, async, null);
    }
    
    /**
     * @see #and(Promise, BiFunction, boolean, Executor)
     */
    public <U, V> Promise<V> and(Promise<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        return and(other, fn, false);
    }
    
    /**
     * @see CompletableFuture#thenAcceptBoth(CompletionStage, BiConsumer)
     */
    public <U> Promise<Void> and(Promise<? extends U> other, BiConsumer<? super T, ? super U> action, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.thenAcceptBoth(other.wrapped, action),
                () -> wrapped.thenAcceptBothAsync(other.wrapped, action),
                () -> wrapped.thenAcceptBothAsync(other.wrapped, action, executor));
    }
    
    /**
     * @see #and(Promise, BiConsumer, boolean, Executor)
     */
    public <U> Promise<Void> and(Promise<? extends U> other, BiConsumer<? super T, ? super U> action, boolean async) {
        return and(other, action, async, null);
    }
    
    /**
     * @see #and(Promise, BiConsumer, boolean, Executor)
     */
    public <U> Promise<Void> and(Promise<? extends U> other, BiConsumer<? super T, ? super U> action) {
        return and(other, action, false);
    }

    /**
     * @see CompletableFuture#runAfterBoth(CompletionStage, Runnable)
     */
    public Promise<Void> and(Promise<?> other, Runnable action, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.runAfterBoth(other.wrapped, action),
                () -> wrapped.runAfterBothAsync(other.wrapped, action),
                () -> wrapped.runAfterBothAsync(other.wrapped, action, executor));
    }
    
    /**
     * @see #and(Promise, Runnable, boolean, Executor)
     */
    public Promise<Void> and(Promise<?> other, Runnable action, boolean async) {
        return and(other, action, async, null);
    }
    
    /**
     * @see #and(Promise, Runnable, boolean, Executor)
     */
    public Promise<Void> and(Promise<?> other, Runnable action) {
        return and(other, action, false);
    }
    
    /**
     * @see CompletableFuture#anyOf(CompletableFuture...)
     */
    public static <T> Promise<T> anyOf(Promise<T>... cfs) {
        return (Promise<T>) new Promise<>(
                CompletableFuture.anyOf(
                        l(cfs).map(it -> it.wrapped).toArray(new CompletableFuture[cfs.length])
        ));
    }

    /**
     * @see CompletableFuture#applyToEither(CompletionStage, Function)
     */
    public <U> Promise<U> or(Promise<? extends T> other, Function<? super T, U> fn, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.applyToEither(other.wrapped, fn),
                () -> wrapped.applyToEitherAsync(other.wrapped, fn),
                () -> wrapped.applyToEitherAsync(other.wrapped, fn, executor));
    }
    
    /**
     * @see #or(Promise, Function, boolean, Executor)
     */
    public <U> Promise<U> or(Promise<? extends T> other, Function<? super T, U> fn, boolean async) {
        return or(other, fn, async, null);
    }
    
    /**
     * @see #or(Promise, Function, boolean, Executor)
     */
    public <U> Promise<U> or(Promise<? extends T> other, Function<? super T, U> fn) {
        return or(other, fn, false);
    }

    /**
     * @see CompletableFuture#acceptEither(CompletionStage, Consumer)
     */
    public Promise<Void> or(Promise<? extends T> other, Consumer<? super T> action, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.acceptEither(other.wrapped, action),
                () -> wrapped.acceptEitherAsync(other.wrapped, action),
                () -> wrapped.acceptEitherAsync(other.wrapped, action, executor));
    }
    
    /**
     * @see #or(Promise, Consumer, boolean, Executor)
     */
    public Promise<Void> or(Promise<? extends T> other, Consumer<? super T> action, boolean async) {
        return or(other, action, async, null);
    }
    
    /**
     * @see #or(Promise, Consumer, boolean, Executor)
     */
    public Promise<Void> or(Promise<? extends T> other, Consumer<? super T> action) {
        return or(other, action, false);
    }

    /**
     * @see CompletableFuture#runAfterEither(CompletionStage, Runnable)
     */
    public Promise<Void> or(Promise<?> other, Runnable action, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.runAfterEither(other.wrapped, action),
                () -> wrapped.runAfterEitherAsync(other.wrapped, action),
                () -> wrapped.runAfterEitherAsync(other.wrapped, action, executor));
    }
    
    /**
     * @see #or(Promise, Runnable, boolean, Executor)
     */
    public Promise<Void> or(Promise<?> other, Runnable action, boolean async) {
        return or(other, action, async, null);
    }
    
    /**
     * @see #or(Promise, Runnable, boolean, Executor)
     */
    public Promise<Void> or(Promise<?> other, Runnable action) {
        return or(other, action, false);
    }
    
    /**
     * Explicitly invokes the respective callback registration function based on whether async and executor are set.
     * Because of the stupidity of the vanilla CompletableFuture API, it may though a NullPointerException
     * if a null executor is provided. Thus we need a more complex switch / case which is implemented in this helper method.
     */
    private <U> Promise<U> apply(boolean async, Executor executor,
            Supplier<CompletableFuture<U>> unlessAsynch,
            Supplier<CompletableFuture<U>> withoutExecutor,
            Supplier<CompletableFuture<U>> all) {
        
            if (!async && executor != null) {
                throw new IllegalArgumentException("Unless async, executor must be null.");
            }
            try {
                return wrap(Choose(!async, unlessAsynch)
                        .Or(async && executor == null, withoutExecutor)
                        .Or(async && executor != null, all)
                        .orThrow());
            }
            catch (Choice.NoValidChoiceException ex) {
                throw new IllegalStateException("No valid argument combination.", ex);
            }
    }
    
    /**
     * @see CompletableFuture#getNumberOfDependents()
     */
    public int getNumberOfDependents() {
        return wrapped.getNumberOfDependents();
    }
    
    /**
     * @see CompletableFuture#isCompletedExceptionally()
     */
    public boolean isCompletedExceptionally() {
        return wrapped.isCompletedExceptionally();
    }
    
    /**
     * @see CompletableFuture#isCancelled()
     */
    public boolean isCancelled() {
        return wrapped.isCancelled();
    }
    
    /**
     * @see CompletableFuture#cancel(boolean)
     */
    public boolean cancel(boolean mayInterruptIfRunning) {
        return wrapped.cancel(mayInterruptIfRunning);
    }
    
    /**
     * @see CompletableFuture#toCompletableFuture()
     */
    public CompletableFuture<T> toCompletableFuture() {
        return wrapped.toCompletableFuture();
    }
    
    /**
     * @see CompletableFuture#completeExceptionally(Throwable)
     */
    public boolean completeExceptionally(Throwable ex) {
        return wrapped.completeExceptionally(ex);
    }
    
    /**
     * @see CompletableFuture#complete(Object)
     */
    public boolean complete(T value) {
        return wrapped.complete(value);
    }
    
    /**
     * @see CompletableFuture#getNow(Object)
     */
    public T getNow(T valueIfAbsent) {
        return wrapped.getNow(valueIfAbsent);
    }
    
    /**
     * @see CompletableFuture#join()
     */
    public T join() {
        return wrapped.join();
    }
    
    /**
     * @see CompletableFuture#get(long, TimeUnit)
     */
    public T get(long timeout, TimeUnit unit) throws ExecutionException, TimeoutException {
        try {
            return wrapped.get(timeout, unit);
        } catch (java.lang.InterruptedException ex) {
            throw new InterruptedException(ex);
        }
    }
    
    /**
     * @see CompletableFuture#get()
     */
    public T get() throws ExecutionException {
        try {
            return wrapped.get();
        } catch (java.lang.InterruptedException ex) {
            throw new InterruptedException(ex);
        }
    }

    /**
     * @see CompletableFuture#isDone()
     */
    public boolean isDone() {
        return wrapped.isDone();
    }
    
    @Override
    public String toString() {
        return "Promise(" + wrapped.toString() + ")";
    }
    
    /**
     * Contrary to the checked {@link Exception} {@link InterruptedException} originally thrown by {@link CompletableFuture}, a {@link Promise}
     * will use this unchecked {@link RuntimeException} only. Reason: 
     * <a href="http://www.codebulb.ch/2015/04/6-rules-of-exception-handling-by-example-part-2.html#checked">
     * it's best practice to use unchecked exceptions for technical exceptions</a>.
     */
    public static class InterruptedException extends RuntimeException {
        public InterruptedException(java.lang.InterruptedException inner) {
            super(inner);
        }
    }
}

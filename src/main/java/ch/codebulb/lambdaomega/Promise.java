package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.L.l;
import static ch.codebulb.lambdaomega.U.*;
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
 * A drop-in-replacement of {@link CompletableFuture} with a simplified, more concise API.
 */
public class Promise<T> {
    public final CompletableFuture<T> wrapped;
    
    public static <T> Promise<T> wrap(CompletableFuture<T> inner) {
        return new Promise<>(inner);
    }
    
    public Promise() {
        this(new CompletableFuture<>());
    }

    public Promise(CompletableFuture<T> inner) {
        this.wrapped = inner;
    }
    
    public static <U> Promise<U> completeAsync(Supplier<U> supplier, Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("Executor parameter must not be null.");
        }
        return new Promise<>(CompletableFuture.supplyAsync(supplier, executor));
    }
    
    public static <U> Promise<U> completeAsync(Supplier<U> supplier) {
        return new Promise<>(CompletableFuture.supplyAsync(supplier));
    }
    
    public static Promise<Void> completeAsync(Runnable runnable, Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("Executor parameter must not be null.");
        }
        return new Promise<>(CompletableFuture.runAsync(runnable, executor));
    }
    
    public static Promise<Void> completeAsync(Runnable runnable) {
        return new Promise<>(CompletableFuture.runAsync(runnable));
    }

    public <U> Promise<U> completed(Function<? super T, ? extends U> fn, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.thenApply(fn),
                () -> wrapped.thenApplyAsync(fn),
                () -> wrapped.thenApplyAsync(fn, executor));
    }
    
    public <U> Promise<U> completed(Function<? super T, ? extends U> fn) {
        return completed(fn, false, null);
    }

    public Promise<Void> completed(Consumer<? super T> action, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.thenAccept(action),
                () -> wrapped.thenAcceptAsync(action),
                () -> wrapped.thenAcceptAsync(action, executor));
    }
    
    public Promise<Void> completed(Consumer<? super T> action) {
        return completed(action, false, null);
    }

    public Promise<Void> completed(Runnable action, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.thenRun(action),
                () -> wrapped.thenRunAsync(action),
                () -> wrapped.thenRunAsync(action, executor));
    }
    
    public Promise<Void> completed(Runnable action) {
        return completed(action, false, null);
    }

    public Promise<T> completedExceptionally(Function<? extends Throwable, ? extends T> fn) {
        return wrap(wrapped.exceptionally((Function<Throwable, ? extends T>) fn));
    }

    public <U> Promise<U> completed(BiFunction<? super T, Throwable, ? extends U> fn, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.handle(fn),
                () -> wrapped.handleAsync(fn),
                () -> wrapped.handleAsync(fn, executor));
    }
    
    public <U> Promise<U> completed(BiFunction<? super T, Throwable, ? extends U> fn) {
        return completed(fn, false, null);
    }

    public Promise<T> completed(BiConsumer<? super T, ? super Throwable> action, boolean async, Executor executor) {
        if (!async && executor != null) {
            throw new IllegalArgumentException("Unless async, executor must be null.");
        }
        return apply(async, executor,
                () -> wrapped.whenComplete(action),
                () -> wrapped.whenCompleteAsync(action),
                () -> wrapped.whenCompleteAsync(action, executor));
    }
    
    public Promise<T> completed(BiConsumer<? super T, ? super Throwable> action) {
        return completed(action, false, null);
    }

    public <U> Promise<U> then(Function<? super T, ? extends CompletionStage<U>> fn, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.thenCompose(fn),
                () -> wrapped.thenComposeAsync(fn),
                () -> wrapped.thenComposeAsync(fn, executor));
    }
    
    public <U> Promise<U> then(Function<? super T, ? extends CompletionStage<U>> fn) {
        return then(fn, false, null);
    }
    
    public static <T> Promise<List<T>> allOf(Promise<T>... cfs) {
        Promise<Void> allDoneFuture = new Promise<>(
                CompletableFuture.allOf(l(cfs).map(it -> it.wrapped)
                        .toArray(new CompletableFuture[cfs.length])
        ));
        return allDoneFuture.completed(it ->
            {return l(cfs).map(future -> future.wrapped.join());}
        );
    }

    public <U, V> Promise<V> and(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.thenCombine(other, fn),
                () -> wrapped.thenCombineAsync(other, fn),
                () -> wrapped.thenCombineAsync(other, fn, executor));
    }
    
    public <U, V> Promise<V> and(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        return and(other, fn, false, null);
    }
    
    public <U> Promise<Void> and(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.thenAcceptBoth(other, action),
                () -> wrapped.thenAcceptBothAsync(other, action),
                () -> wrapped.thenAcceptBothAsync(other, action, executor));
    }
    
    public <U> Promise<Void> and(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action) {
        return and(other, action, false, null);
    }

    public Promise<Void> and(CompletionStage<?> other, Runnable action, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.runAfterBoth(other, action),
                () -> wrapped.runAfterBothAsync(other, action),
                () -> wrapped.runAfterBothAsync(other, action, executor));
    }
    
    public Promise<Void> and(CompletionStage<?> other, Runnable action) {
        return and(other, action, false, null);
    }
    
    public static <T> Promise<T> anyOf(Promise<T>... cfs) {
        return (Promise<T>) new Promise<>(
                CompletableFuture.anyOf(
                        l(cfs).map(it -> it.wrapped).toArray(new CompletableFuture[cfs.length])
        ));
    }

    public <U> Promise<U> or(CompletionStage<? extends T> other, Function<? super T, U> fn, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.applyToEither(other, fn),
                () -> wrapped.applyToEitherAsync(other, fn),
                () -> wrapped.applyToEitherAsync(other, fn, executor));
    }
    
    public <U> Promise<U> or(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        return or(other, fn, false, null);
    }

    public Promise<Void> or(CompletionStage<? extends T> other, Consumer<? super T> action, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.acceptEither(other, action),
                () -> wrapped.acceptEitherAsync(other, action),
                () -> wrapped.acceptEitherAsync(other, action, executor));
    }
    
    public Promise<Void> or(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return or(other, action, false, null);
    }

    public Promise<Void> or(CompletionStage<?> other, Runnable action, boolean async, Executor executor) {
        return apply(async, executor,
                () -> wrapped.runAfterEither(other, action),
                () -> wrapped.runAfterEitherAsync(other, action),
                () -> wrapped.runAfterEitherAsync(other, action, executor));
    }
    
    public Promise<Void> or(CompletionStage<?> other, Runnable action) {
        return or(other, action, false, null);
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
    
    public int getNumberOfDependents() {
        return wrapped.getNumberOfDependents();
    }
    
    public boolean isCompletedExceptionally() {
        return wrapped.isCompletedExceptionally();
    }
    
    public boolean isCancelled() {
        return wrapped.isCancelled();
    }
    
    public boolean cancel(boolean mayInterruptIfRunning) {
        return wrapped.cancel(mayInterruptIfRunning);
    }
    
    public CompletableFuture<T> toCompletableFuture() {
        return wrapped.toCompletableFuture();
    }
    
    public boolean completeExceptionally(Throwable ex) {
        return wrapped.completeExceptionally(ex);
    }
    
    public boolean complete(T value) {
        return wrapped.complete(value);
    }
    
    public T getNow(T valueIfAbsent) {
        return wrapped.getNow(valueIfAbsent);
    }
    
    public T join() {
        return wrapped.join();
    }
    
    public T get(long timeout, TimeUnit unit) throws ExecutionException, TimeoutException {
        try {
            return wrapped.get(timeout, unit);
        } catch (java.lang.InterruptedException ex) {
            throw new InterruptedException(ex);
        }
    }
    
    public T get() throws ExecutionException {
        try {
            return wrapped.get();
        } catch (java.lang.InterruptedException ex) {
            throw new InterruptedException(ex);
        }
    }

    public boolean isDone() {
        return wrapped.isDone();
    }
    
    @Override
    public String toString() {
        return wrapped.toString();
    }
    
    public static class InterruptedException extends RuntimeException {
        public InterruptedException(java.lang.InterruptedException inner) {
            super(inner);
        }
    }
}

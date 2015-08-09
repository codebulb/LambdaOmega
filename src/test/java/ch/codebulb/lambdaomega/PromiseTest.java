package ch.codebulb.lambdaomega;

import static ch.codebulb.lambdaomega.TestUtil.*;
import static ch.codebulb.lambdaomega.L.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

// CHECKME Concurrency issues. If one test fails, others may spuriously fail or throw exceptions as well.
public class PromiseTest {
    private static final Executor DEFAULT_EXECUTOR = ForkJoinPool.commonPool();
    
    CompletableFuture<String> expectedPromise = new CompletableFuture<>();
    Promise<String> actualPromise = new Promise<>();
    Promise<String> actualPromiseAsync = new Promise<>();
    Promise<String> actualPromiseAsyncExec = new Promise<>();
    
    Promise<String> async = new Promise<>();
    Promise<String> asyncExec = new Promise<>();
    
    private static final String OUTPUT = "Output";
    private static final String OUTPUT_MODIFIED = "Output!";
    private static final String ANY_OUTPUT = "Anything";
    private static final L<String> OUTPUTS = l(OUTPUT, OUTPUT, OUTPUT);
    private static final L<String> OUTPUTS_MODIFIED = l(OUTPUT_MODIFIED, OUTPUT_MODIFIED, OUTPUT_MODIFIED);
    
    private final L<String> expected = l();
    private final L<String> actual = l();
    private final L<String> actual1 = l();
    private final L<String> actual2 = l();
    private final L<String> actual3 = l();
     
    private Throwable expectedException;
    private Throwable actualException;
    
    private static final L<String> ERRORS = l(3, (it) -> MyCompletableFutureTestException.class.getSimpleName());
    private static final L<String> ERRORS_WARPPED = l(3, (it) -> MyWrappedCompletableFutureTestException.class.getSimpleName());
    
    private static class MyCompletableFutureTestException extends RuntimeException {
    }
    
    private static class MyWrappedCompletableFutureTestException extends RuntimeException {
        public MyWrappedCompletableFutureTestException(MyCompletableFutureTestException cause) {
            super(cause);
        }
    }
    
    @Test
    public void testCompleteAsync() throws InterruptedException, ExecutionException {
        String expected = CompletableFuture.supplyAsync(() -> OUTPUT).get();
        String actual = Promise.completeAsync(() -> OUTPUT).get();
        
        TestUtil.assertEquals(OUTPUT, expected, actual);
    }
    
    @Test
    public void testCompleteAsyncRunnable() throws InterruptedException, ExecutionException {
        CompletableFuture.runAsync(() -> expected.a(OUTPUT));
        Promise.completeAsync(() -> {actual.a(OUTPUT);}).get();
        
        assertEquals(OUTPUT, expected.get(0), actual.get(0));
    }
    
    
    @Test
    public void testThenApplyCompleted() throws InterruptedException, ExecutionException {
        expectedPromise.thenApply(it -> expected.a(it, it, it));
        expectedPromise.complete(OUTPUT);
        expectedPromise.join();
        
        actualPromise.completed(it -> {return actual1.a(it);});
        executeAsync(actualPromiseAsync.completed(it -> {return actual2.a(it);}, true, null),
        actualPromiseAsyncExec.completed(it -> {return actual3.a(it);}, true, DEFAULT_EXECUTOR));
        actualPromise.complete(OUTPUT);
        actualPromiseAsync.complete(OUTPUT);
        actualPromiseAsyncExec.complete(OUTPUT);
        waitForAsyncCompletion();
        
        Assert.assertEquals(OUTPUTS.l, expected.l);
        Assert.assertEquals(OUTPUTS.l, l(actual1, actual2, actual3).flatten());
    }
    
    private <T> void executeAsync(Promise<T> asyncPromise, Promise<T> asyncExecPromise) {
        asyncPromise.completed((it, err) -> {async.complete(null);});
        asyncExecPromise.completed((it, err) -> {asyncExec.complete(null);});
    }
    
    private void waitForAsyncCompletion() {
        actualPromise.join();
        actualPromiseAsync.join();
        async.join();
        actualPromiseAsyncExec.join();
        asyncExec.join();
    }
    
    private void waitForAsyncCompletionWithErrors() {
        try {
            actualPromise.get();
            fail();
        } catch (ExecutionException ex) {
            // expected
        }
        try {
            actualPromiseAsync.get();
            fail();
        } catch (ExecutionException ex) {
            // expected
        }
        async.join();
        try {
            actualPromiseAsyncExec.get();
            fail();
        } catch (ExecutionException ex) {
            // expected
        }
        asyncExec.join();
    }
    
    @Test
    public void testThenRunCompleted() throws InterruptedException, ExecutionException {
        expectedPromise.thenRun(() -> expected.a(OUTPUT).a(OUTPUT).a(OUTPUT));
        expectedPromise.complete(ANY_OUTPUT);
        expectedPromise.join();
        
        actualPromise.completed(() -> {actual1.a(OUTPUT);});
        executeAsync(actualPromiseAsync.completed(() -> {actual2.a(OUTPUT);}, true, null),
            actualPromiseAsyncExec.completed(() -> {actual3.a(OUTPUT);}, true, DEFAULT_EXECUTOR));
        actualPromise.complete(ANY_OUTPUT);
        actualPromiseAsync.complete(ANY_OUTPUT);
        actualPromiseAsyncExec.complete(ANY_OUTPUT);
        waitForAsyncCompletion();

        Assert.assertEquals(OUTPUTS.l, expected.l);
        // prevent spurious ConcurrentModificationException
        Assert.assertEquals(OUTPUTS.l, l(actual1, actual2, actual3).flatten());
    }
    
    @Test
    public void testThenAcceptCompleted() throws InterruptedException, ExecutionException {
        expectedPromise.thenApply((it) -> OUTPUT_MODIFIED).thenAccept(it -> expected.a(it, it, it));
        expectedPromise.complete(ANY_OUTPUT);
        expectedPromise.join();
        
        actualPromise.completed((it) -> OUTPUT_MODIFIED).completed(it -> {actual1.a(it);});
        executeAsync(actualPromiseAsync.completed((it) -> OUTPUT_MODIFIED).completed(it -> {actual2.a(it);}, true, null),
        actualPromiseAsyncExec.completed((it) -> OUTPUT_MODIFIED).completed(it -> {actual3.a(it);}, true, DEFAULT_EXECUTOR));
        actualPromise.complete(ANY_OUTPUT);
        actualPromiseAsync.complete(ANY_OUTPUT);
        actualPromiseAsyncExec.complete(ANY_OUTPUT);
        waitForAsyncCompletion();
        
        Assert.assertEquals(OUTPUTS_MODIFIED.l, expected.l);
        // prevent spurious ConcurrentModificationException
        Assert.assertEquals(OUTPUTS_MODIFIED.l, l(actual1, actual2, actual3).flatten());
    }
    
    @Test
    public void testCompletedExceptionally() throws InterruptedException, ExecutionException {
        expectedPromise.exceptionally((it) -> {expectedException = it; throw new RuntimeException();}).thenRun(() -> expected.a(ANY_OUTPUT));
        expectedPromise.completeExceptionally(new MyCompletableFutureTestException());
        
        actualPromise.completedExceptionally((it) -> {actualException = it; throw new RuntimeException();}).completed(() -> actual.a(ANY_OUTPUT));
        actualPromise.completeExceptionally(new MyCompletableFutureTestException());
        
        assertEquals(MyCompletableFutureTestException.class, expectedException.getClass(), actualException.getClass());
        assertEquals(0, expected.size(), actual.size());
    }
    
    @Test
    public void testCompletedExceptionallyWithRecover() throws InterruptedException, ExecutionException {
        expectedPromise.exceptionally((it) -> {expectedException = it; return it.getClass().getSimpleName();}).thenApply((it) -> expected.a(it));
        expectedPromise.completeExceptionally(new MyCompletableFutureTestException());
        
        actualPromise.completedExceptionally((it) -> {actualException = it; return it.getClass().getSimpleName();}).completed((it) -> {actual.a(it);});
        actualPromise.completeExceptionally(new MyCompletableFutureTestException());
        
        assertEquals(MyCompletableFutureTestException.class, expectedException.getClass(), actualException.getClass());
        assertEquals(MyCompletableFutureTestException.class.getSimpleName(), expected.get(0), actual.get(0));
    }
    
    @Test
    public void testHandleCompleted() throws InterruptedException, ExecutionException {
        expectedPromise.handle((it, err) -> {if (err != null) {fail();}; return expected.a(it, it, it);});
        expectedPromise.complete(OUTPUT);
        expectedPromise.join();
        
        actualPromise.completed((it, err) -> {if (err != null) {fail();}; return actual1.a(it);});
        executeAsync(actualPromiseAsync.completed((it, err) -> {if (err != null) {fail();}; return actual2.a(it);}, true, null),
        actualPromiseAsyncExec.completed((it, err) -> {if (err != null) {fail();}; return actual3.a(it);}, true, DEFAULT_EXECUTOR));
        actualPromise.complete(OUTPUT);
        actualPromiseAsync.complete(OUTPUT);
        actualPromiseAsyncExec.complete(OUTPUT);
        waitForAsyncCompletion();
        
        Assert.assertEquals(OUTPUTS.l, expected.l);
        // prevent spurious ConcurrentModificationException
        Assert.assertEquals(OUTPUTS.l, l(actual1, actual2, actual3).flatten());
    }
    
    @Test
    public void testHandleCompletedWithError() throws InterruptedException {
        expectedPromise.handle((it, err) -> {if (err == null) {fail();}; 
            return expected.a(err.getClass().getSimpleName(), err.getClass().getSimpleName(), err.getClass().getSimpleName());});
        expectedPromise.completeExceptionally(new MyCompletableFutureTestException());
        try {
            expectedPromise.get();
            fail();
        } catch (ExecutionException ex) {
            // expected
        }
        
        actualPromise.completed((it, err) -> {if (err == null) {fail();}; return actual1.a(err.getClass().getSimpleName());});
        executeAsync(actualPromiseAsync.completed((it, err) -> {if (err == null) {fail();}; return actual2.a(err.getClass().getSimpleName());}, true, null),
        actualPromiseAsyncExec.completed((it, err) -> {if (err == null) {fail();}; return actual3.a(err.getClass().getSimpleName());}, true, DEFAULT_EXECUTOR));
        actualPromise.completeExceptionally(new MyCompletableFutureTestException());
        actualPromiseAsync.completeExceptionally(new MyCompletableFutureTestException());
        actualPromiseAsyncExec.completeExceptionally(new MyCompletableFutureTestException());
        waitForAsyncCompletionWithErrors();
        
        Assert.assertEquals(ERRORS.l, expected.l);
        // prevent spurious ConcurrentModificationException
        Assert.assertEquals(ERRORS.l, l(actual1, actual2, actual3).flatten());
    }
    
    @Test
    public void testWhenCompleteCompleted() throws InterruptedException, ExecutionException {
        expectedPromise.thenApply((it) -> OUTPUT_MODIFIED).whenComplete((it, err) -> {if (err != null) {fail();}; expected.a(it, it, it);});
        expectedPromise.complete(ANY_OUTPUT);
        expectedPromise.join();
        
        actualPromise.completed((it) -> OUTPUT_MODIFIED).completed((it, err) -> {if (err != null) {fail();}; actual1.a(it);});
        executeAsync(actualPromiseAsync.completed((it) -> OUTPUT_MODIFIED).completed((it, err) -> {if (err != null) {fail();}; actual2.a(it);}, true, null),
        actualPromiseAsyncExec.completed((it) -> OUTPUT_MODIFIED).completed((it, err) -> {if (err != null) {fail();}; actual3.a(it);}, true, DEFAULT_EXECUTOR));
        actualPromise.complete(ANY_OUTPUT);
        actualPromiseAsync.complete(ANY_OUTPUT);
        actualPromiseAsyncExec.complete(ANY_OUTPUT);
        waitForAsyncCompletion();
        
        Assert.assertEquals(OUTPUTS_MODIFIED.l, expected.l);
        // prevent spurious ConcurrentModificationException
        Assert.assertEquals(OUTPUTS_MODIFIED.l, l(actual1, actual2, actual3).flatten());
    }
    
    @Test
    public void testWhenCompleteCompletedWithError() throws InterruptedException {
        expectedPromise.exceptionally((it) -> {throw new MyWrappedCompletableFutureTestException((MyCompletableFutureTestException) it);})
                .whenComplete((it, err) -> {if (err == null) {fail();}; 
                    expected.a(err.getCause().getClass().getSimpleName(), err.getCause().getClass().getSimpleName(), err.getCause().getClass().getSimpleName());});
        expectedPromise.completeExceptionally(new MyCompletableFutureTestException());
        try {
            expectedPromise.get();
            fail();
        } catch (ExecutionException ex) {
            // expected
        }
        
        actualPromise.completedExceptionally((MyCompletableFutureTestException it) -> {throw new MyWrappedCompletableFutureTestException(it);})
                .completed((it, err) -> {if (err == null) {fail();}; return actual1.a(err.getCause().getClass().getSimpleName());});
        executeAsync(actualPromiseAsync.completedExceptionally((MyCompletableFutureTestException it) -> {throw new MyWrappedCompletableFutureTestException(it);})
                .completed((it, err) -> {if (err == null) {fail();}; return actual2.a(err.getCause().getClass().getSimpleName());}, true, null),
        actualPromiseAsyncExec.completedExceptionally((MyCompletableFutureTestException it) -> {throw new MyWrappedCompletableFutureTestException(it);})
                .completed((it, err) -> {if (err == null) {fail();}; return actual3.a(err.getCause().getClass().getSimpleName());}, true, DEFAULT_EXECUTOR));
        actualPromise.completeExceptionally(new MyCompletableFutureTestException());
        actualPromiseAsync.completeExceptionally(new MyCompletableFutureTestException());
        actualPromiseAsyncExec.completeExceptionally(new MyCompletableFutureTestException());
        waitForAsyncCompletionWithErrors();
        
        Assert.assertEquals(ERRORS_WARPPED.l, expected.l);
        // prevent spurious ConcurrentModificationException
        Assert.assertEquals(ERRORS_WARPPED.l, l(actual1, actual2, actual3).flatten());
    }
    
    @Test
    public void testThen() throws InterruptedException, ExecutionException {
        CompletableFuture<String> expectedPromise1 = new CompletableFuture<>();
        expectedPromise1.thenAccept(it -> expected.a(it, it, it));
        expectedPromise.thenCompose(it -> expectedPromise1);
        expectedPromise.complete(ANY_OUTPUT);
        expectedPromise1.complete(OUTPUT);
        expectedPromise.join();
        expectedPromise1.join();
        
        Promise<String> actualPromise1 = new Promise<>();
        actualPromise1.completed(it -> {actual.a(it);});
        Promise<String> actualPromise2 = new Promise<>();
        actualPromise2.completed(it -> {actual.a(it);});
        Promise<String> actualPromise3 = new Promise<>();
        actualPromise3.completed(it -> {actual.a(it);});
        actualPromise.then(it -> actualPromise1.wrapped);
        executeAsync(actualPromiseAsync.then(it -> actualPromise2.wrapped, true, null),
        actualPromiseAsyncExec.then(it -> actualPromise3.wrapped, true, DEFAULT_EXECUTOR));
        actualPromise.complete(ANY_OUTPUT);
        actualPromiseAsync.complete(ANY_OUTPUT);
        actualPromiseAsyncExec.complete(ANY_OUTPUT);
        actualPromise1.complete(OUTPUT);
        actualPromise2.complete(OUTPUT);
        actualPromise3.complete(OUTPUT);
        waitForAsyncCompletion();
        actualPromise1.join();
        actualPromise2.join();
        actualPromise3.join();
        
        Assert.assertEquals(OUTPUTS.l, expected.l);
        Assert.assertEquals(OUTPUTS.l, actual.l);
    }
    
    @Test
    public void testAllOf() throws InterruptedException, ExecutionException {
        CompletableFuture<String> expectedPromise1 = new CompletableFuture<>();
        
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(expectedPromise, expectedPromise1);
        CompletableFuture<List<String>> expectedPromiseAll = allDoneFuture.thenApply(it ->
            {return l(expectedPromise, expectedPromise1).map(future -> future.join());}
        );
        expectedPromiseAll.thenAccept(it -> expected.a(it.get(0)));
        expectedPromise.complete(OUTPUT);
        expectedPromise1.complete(OUTPUT);
        expectedPromise.join();
        expectedPromise1.join();
        
        Promise<String> actualPromise1 = new Promise<>();
        Promise<List<String>> actualPromiseAllOf = Promise.allOf(actualPromise, actualPromise1);
        actualPromiseAllOf.completed(it -> {actual.a(it.get(0));});
        actualPromise1.complete(OUTPUT);
        actualPromise.complete(OUTPUT);
        actualPromise.join();
        actualPromise1.join();
        
        Assert.assertEquals(OUTPUT, expected.get(0));
        Assert.assertEquals(OUTPUT, actual.get(0));
    }
    
    @Test
    public void testThenCombineAnd() throws InterruptedException, ExecutionException {
        CompletableFuture<String> expectedPromise1 = new CompletableFuture<>();
        expectedPromise.thenCombine(expectedPromise1, (a, b) -> OUTPUT_MODIFIED).thenAccept(it -> expected.a(it, it, it));
        expectedPromise.complete(ANY_OUTPUT);
        expectedPromise1.complete(ANY_OUTPUT);
        expectedPromise.join();
        expectedPromise1.join();
        
        Promise<String> actualPromise1 = new Promise<>();
        actualPromise.and(actualPromise1.wrapped, (a, b) -> OUTPUT_MODIFIED).completed(it -> {return actual.a(it);});
        executeAsync(actualPromise.and(actualPromiseAsync.wrapped, (a, b) -> OUTPUT_MODIFIED).completed(it -> {return actual.a(it);}),
        actualPromiseAsyncExec.and(actualPromise1.wrapped, (a, b) -> OUTPUT_MODIFIED).completed(it -> {return actual.a(it);}));
        actualPromise1.complete(ANY_OUTPUT);
        actualPromise.complete(ANY_OUTPUT);
        actualPromiseAsync.complete(ANY_OUTPUT);
        actualPromiseAsyncExec.complete(ANY_OUTPUT);
        waitForAsyncCompletion();
        actualPromise1.join();
        
        Assert.assertEquals(OUTPUTS_MODIFIED.l, expected.l);
        Assert.assertEquals(OUTPUTS_MODIFIED.l, actual.l);
    }
    
    @Test
    public void testThenAcceptBothAnd() throws InterruptedException, ExecutionException {
        CompletableFuture<String> expectedPromise1 = new CompletableFuture<>();
        expectedPromise.thenAcceptBoth(expectedPromise1, (a, b) -> expected.a(a, a, a));
        expectedPromise.complete(OUTPUT);
        expectedPromise1.complete(ANY_OUTPUT);
        expectedPromise.join();
        expectedPromise1.join();
        
        Promise<String> actualPromise1 = new Promise<>();
        actualPromise.and(actualPromise1.wrapped, (a, b) -> {actual.a(a);});
        executeAsync(actualPromise.and(actualPromiseAsync.wrapped, (a, b) -> {actual.a(a);}),
        actualPromiseAsyncExec.and(actualPromise1.wrapped, (a, b) -> {actual.a(a);}));
        actualPromise1.complete(ANY_OUTPUT);
        actualPromise.complete(OUTPUT);
        actualPromiseAsync.complete(OUTPUT);
        actualPromiseAsyncExec.complete(OUTPUT);
        waitForAsyncCompletion();
        actualPromise1.join();
        
        Assert.assertEquals(OUTPUTS.l, expected.l);
        Assert.assertEquals(OUTPUTS.l, actual.l);
    }
    
    @Test
    public void testRunAfterBothAnd() throws InterruptedException, ExecutionException {
        CompletableFuture<String> expectedPromise1 = new CompletableFuture<>();
        expectedPromise.runAfterBoth(expectedPromise1, () -> expected.a(OUTPUT, OUTPUT, OUTPUT));
        expectedPromise.complete(ANY_OUTPUT);
        expectedPromise1.complete(ANY_OUTPUT);
        expectedPromise.join();
        expectedPromise1.join();
        
        Promise<String> actualPromise1 = new Promise<>();
        actualPromise.and(actualPromise1.wrapped, () -> {actual.a(OUTPUT);});
        executeAsync(actualPromise.and(actualPromiseAsync.wrapped, () -> {actual.a(OUTPUT);}),
        actualPromiseAsyncExec.and(actualPromise1.wrapped, () -> {actual.a(OUTPUT);}));
        actualPromise1.complete(ANY_OUTPUT);
        actualPromise.complete(ANY_OUTPUT);
        actualPromiseAsync.complete(ANY_OUTPUT);
        actualPromiseAsyncExec.complete(ANY_OUTPUT);
        waitForAsyncCompletion();
        actualPromise1.join();
        
        Assert.assertEquals(OUTPUTS.l, expected.l);
        Assert.assertEquals(OUTPUTS.l, actual.l);
    }
    
    @Test
    public void testAnyOf() throws InterruptedException, ExecutionException {
        CompletableFuture<String> expectedPromise1 = new CompletableFuture<>();
        
        CompletableFuture<Object> expectedPromiseAny = CompletableFuture.anyOf(expectedPromise, expectedPromise1);
        expectedPromiseAny.thenAccept(it -> expected.a(((String)it)));
        expectedPromise.complete(OUTPUT);
        expectedPromise1.complete(OUTPUT);
        expectedPromise.join();
        expectedPromise1.join();
        
        Promise<String> actualPromise1 = new Promise<>();
        Promise<String> actualPromiseAnyOf = Promise.anyOf(actualPromise, actualPromise1);
        actualPromiseAnyOf.completed(it -> {actual.a(it);});
        actualPromise1.complete(OUTPUT);
        actualPromise.complete(OUTPUT);
        actualPromise.join();
        actualPromise1.join();
        
        Assert.assertEquals(OUTPUT, expected.get(0));
        Assert.assertEquals(OUTPUT, actual.get(0));
    }
    
    @Test
    public void testApplyToEitherOr() throws InterruptedException, ExecutionException {
        CompletableFuture<String> expectedPromise1 = new CompletableFuture<>();
        expectedPromise.applyToEither(expectedPromise1, (it) -> it).thenAccept(it -> expected.a(it, it, it));
        expectedPromise.complete(OUTPUT);
        expectedPromise1.complete(OUTPUT);
        expectedPromise.join();
        expectedPromise1.join();
        
        Promise<String> actualPromise1 = new Promise<>();
        actualPromise.or(actualPromise1.wrapped, (it) -> it).completed(it -> {return actual.a(it);});
        executeAsync(actualPromise.or(actualPromiseAsync.wrapped, (it) -> it).completed(it -> {return actual.a(it);}),
        actualPromiseAsyncExec.or(actualPromise1.wrapped, (it) -> it).completed(it -> {return actual.a(it);}));
        actualPromise1.complete(OUTPUT);
        actualPromise.complete(OUTPUT);
        actualPromiseAsync.complete(OUTPUT);
        actualPromiseAsyncExec.complete(OUTPUT);
        waitForAsyncCompletion();
        actualPromise1.join();
        
        Assert.assertEquals(OUTPUTS.l, expected.l);
        Assert.assertEquals(OUTPUTS.l, actual.l);
    }
    
    @Test
    public void testAcceptEitherOr() throws InterruptedException, ExecutionException {
        CompletableFuture<String> expectedPromise1 = new CompletableFuture<>();
        expectedPromise.acceptEither(expectedPromise1, (it) -> expected.a(it, it, it));
        expectedPromise.complete(OUTPUT);
        expectedPromise1.complete(OUTPUT);
        expectedPromise.join();
        expectedPromise1.join();
        
        Promise<String> actualPromise1 = new Promise<>();
        actualPromise.or(actualPromise1.wrapped, (it) -> {actual.a(it);});
        executeAsync(actualPromise.or(actualPromiseAsync.wrapped, (it) -> {actual.a(it);}),
        actualPromiseAsyncExec.or(actualPromise1.wrapped, (it) -> {actual.a(it);}));
        actualPromise1.complete(OUTPUT);
        actualPromise.complete(OUTPUT);
        actualPromiseAsync.complete(OUTPUT);
        actualPromiseAsyncExec.complete(OUTPUT);
        waitForAsyncCompletion();
        actualPromise1.join();
        
        Assert.assertEquals(OUTPUTS.l, expected.l);
        Assert.assertEquals(OUTPUTS.l, actual.l);
    }
    
    @Test
    public void testRunAfterEitherOr() throws InterruptedException, ExecutionException {
        CompletableFuture<String> expectedPromise1 = new CompletableFuture<>();
        expectedPromise.runAfterEither(expectedPromise1, () -> expected.a(OUTPUT, OUTPUT, OUTPUT));
        expectedPromise.complete(ANY_OUTPUT);
        expectedPromise1.complete(ANY_OUTPUT);
        expectedPromise.join();
        expectedPromise1.join();
        
        Promise<String> actualPromise1 = new Promise<>();
        actualPromise.or(actualPromise1.wrapped, () -> {actual.a(OUTPUT);});
        executeAsync(actualPromise.or(actualPromiseAsync.wrapped, () -> {actual.a(OUTPUT);}),
        actualPromiseAsyncExec.or(actualPromise1.wrapped, () -> {actual.a(OUTPUT);}));
        actualPromise1.complete(ANY_OUTPUT);
        actualPromise.complete(ANY_OUTPUT);
        actualPromiseAsync.complete(ANY_OUTPUT);
        actualPromiseAsyncExec.complete(ANY_OUTPUT);
        waitForAsyncCompletion();
        actualPromise1.join();
        
        Assert.assertEquals(OUTPUTS.l, expected.l);
        Assert.assertEquals(OUTPUTS.l, actual.l);
    }
}

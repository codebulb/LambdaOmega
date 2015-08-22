
package ch.codebulb.lambdaomega;

import ch.codebulb.lambdaomega.M.E;
import static ch.codebulb.lambdaomega.U.in;
import ch.codebulb.lambdaomega.abstractions.OmegaObject;
import ch.codebulb.lambdaomega.abstractions.ReadonlyIndexedI;
import ch.codebulb.lambdaomega.abstractions.StreamableI;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The "C" stands for "common collection". This is the base class of all LambdaOmega collections which implements common functionality.<p>
 * 
 * It also provides common static helper methods for collections.
 *
 * @param <T> the type of an entry when accessed sequentially
 * @param <K> the type of a key for indexed access
 * @param <V> the type of a value for indexed access
 */
// TODO Complete implementation with more converters
public abstract class C<T, K, V> extends OmegaObject implements StreamableI, ReadonlyIndexedI<K, V> {
    private boolean parallel;
    protected Function<K, V> defaultFunction;
    
    public abstract Collection<T> toCollection();
    
    /**
     * Depending on {@link #isParallel()}, returns a parallel or a sequential {@link Stream}.
     */
    public Stream<T> stream() {
        return isParallel() ? toCollection().parallelStream() : toCollection().stream();
    }
    
    @Override
    public boolean isParallel() {
        return parallel;
    }
    
    /**
     * Turns the wrapped data structure parallel streamed.
     */
    public C<T, K, V> Parallel() {
        parallel = true;
        return this;
    }
    
    /**
     * @see #Parallel()
     */
    public C<T, K, V> Par() {
        return Parallel();
    }
    
    /**
     * Turns the wrapped data structure sequentially streamed.
     */
    public C<T, K, V> Sequential() {
        parallel = false;
        return this;
    }
    
    /**
     * @see #Sequential()
     */
    public C<T, K, V> Seq() {
        return Sequential();
    }
    
    @Override
    public C<T, K, V> WithDefault(Function<K, V> defaultValue) {
        this.defaultFunction = defaultValue;
        return this;
    }
    
    public <C> C to(Class<C> format) {
        return to(stream(), format);
    }
    
    public static <T, C> C to(Stream<T> stream, Class<C> format) {
        // We have to copy the stream just in case we want to use its size (cannot open a stream twice)
        List<T> collectedStream = stream.collect(Collectors.toList());
        
        if (in(it -> format.isAssignableFrom(it),
                ArrayBlockingQueue.class,
                ArrayDeque.class,
                ArrayList.class,
                ConcurrentLinkedDeque.class,
                ConcurrentLinkedQueue.class,
                ConcurrentSkipListSet.class,
                CopyOnWriteArrayList.class,
                CopyOnWriteArraySet.class,
                HashSet.class,
                LinkedBlockingDeque.class,
                LinkedBlockingQueue.class,
                LinkedHashSet.class,
                LinkedList.class,
                LinkedTransferQueue.class,
                PriorityBlockingQueue.class,
                PriorityQueue.class,
                Stack.class,
                TreeSet.class)) {
            return (C)collectedStream.stream().collect(Collectors.toCollection(() -> {
            // all java.util.* implementations of Collection
            if (format.isAssignableFrom(ArrayBlockingQueue.class)) {
                return new ArrayBlockingQueue<T>(collectedStream.size());
            }
            if (format.isAssignableFrom(ArrayDeque.class)) {
                return new ArrayDeque<T>(collectedStream.size());
            }
            if (format.isAssignableFrom(ArrayList.class)) {
                return new ArrayList<T>();
            }
            if (format.isAssignableFrom(ConcurrentLinkedDeque.class)) {
                return new ConcurrentLinkedDeque<T>();
            }
            if (format.isAssignableFrom(ConcurrentLinkedQueue.class)) {
                return new ConcurrentLinkedQueue<T>();
            }
            if (format.isAssignableFrom(ConcurrentSkipListSet.class)) {
                return new ConcurrentSkipListSet<T>();
            }
            if (format.isAssignableFrom(CopyOnWriteArrayList.class)) {
                return new CopyOnWriteArrayList<T>();
            }
            if (format.isAssignableFrom(CopyOnWriteArraySet.class)) {
                return new CopyOnWriteArraySet<T>();
            }
            if (format.isAssignableFrom(HashSet.class)) {
                return new HashSet<T>();
            }
            if (format.isAssignableFrom(LinkedBlockingDeque.class)) {
                return new LinkedBlockingDeque<T>();
            }
            if (format.isAssignableFrom(LinkedBlockingQueue.class)) {
                return new LinkedBlockingQueue<T>();
            }
            if (format.isAssignableFrom(LinkedHashSet.class)) {
                return new LinkedHashSet<T>();
            }
            if (format.isAssignableFrom(LinkedList.class)) {
                return new LinkedList<T>();
            }
            if (format.isAssignableFrom(LinkedTransferQueue.class)) {
                return new LinkedTransferQueue<T>();
            }
            if (format.isAssignableFrom(PriorityBlockingQueue.class)) {
                return new PriorityBlockingQueue<T>();
            }
            if (format.isAssignableFrom(PriorityQueue.class)) {
                return new PriorityQueue<T>();
            }
            if (format.isAssignableFrom(Stack.class)) {
                return new Stack<T>();
            }
            // TODO What about SynchronousQueue?
            if (format.isAssignableFrom(TreeSet.class)) {
                return new TreeSet<T>();
            }
            // Vector is considered "obsolete"
            
            throw new IllegalArgumentException("Return type not supported: " + format);
        }));
        }
        
        if (in(it -> format.isAssignableFrom(it), 
                ConcurrentHashMap.class,
                ConcurrentSkipListMap.class)) {
            return (C)collectedStream.stream().collect(Collectors.toConcurrentMap(it -> ((E)it).k, it -> ((E)it).v, (x, y) -> x, () -> {
            // all implementations of ConcurrentMap
            if (format.isAssignableFrom(ConcurrentHashMap.class)) {
                return new ConcurrentHashMap<>();
            }
            if (format.isAssignableFrom(ConcurrentSkipListMap.class)) {
                return new ConcurrentSkipListMap<>();
            }
            throw new IllegalArgumentException("Return type not supported: " + format);
        }));
        }
        
        if (in(it -> format.isAssignableFrom(it), 
                HashMap.class,
                Hashtable.class,
                IdentityHashMap.class,
                LinkedHashMap.class,
                Properties.class,
                TreeMap.class,
                WeakHashMap.class)) {
            return (C)collectedStream.stream().collect(Collectors.toMap(it -> ((E)it).k, it -> ((E)it).v, (x, y) -> x, () -> {
            // all other java.util.* implementations of Map
            if (format.isAssignableFrom(HashMap.class)) {
                return new HashMap<>();
            }
            if (format.isAssignableFrom(Hashtable.class)) {
                return new Hashtable<>();
            }
            if (format.isAssignableFrom(IdentityHashMap.class)) {
                return new IdentityHashMap<>();
            }
            if (format.isAssignableFrom(LinkedHashMap.class)) {
                return new LinkedHashMap<>();
            }
            if (format.isAssignableFrom(Properties.class)) {
                return new Properties();
            }
            if (format.isAssignableFrom(TreeMap.class)) {
                return new TreeMap<>();
            }
            if (format.isAssignableFrom(WeakHashMap.class)) {
                return new WeakHashMap<>();
            }
            throw new IllegalArgumentException("Return type not supported: " + format);
        }));
        }
        
        throw new IllegalArgumentException("Return type not supported: " + format);
    }
    
    public static <T> Stream<T> toStream(Collection<T> collection) {
        return StreamSupport.stream(collection.spliterator(), false);
    }
    
    public static <T> Stream<T> toStream(T... ts) {
        return Arrays.stream(ts);
    }
    
    public static <T> Stream<T> toStream(M map) {
        return map.stream();
    }
    
    public static <T> List<T> toList(Collection<T> collection) {
        return new ArrayList<>(collection);
    }
    
    public static <T> List<T> toList(T... ts) {
        return Arrays.asList(ts);
    }
    
    public static <T> List<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toList());
    }
    
    public static <T> Set<T> toSet(Collection<T> collection) {
        return new HashSet<>(collection);
    }
    
    public static <T> Set<T> toSet(T... ts) {
        return toStream(ts).collect(Collectors.toSet());
    }
    
    /**
     * A shorthand for subsequent application of {@link #toStream(Object...)} on the <code>elements</code> provided,
     * {@link Stream#map(Function)} with the <code>function</code> provided, and {@link Stream#collect(java.util.stream.Collector)}ing
     * the results in a {@link List}.
     */
    public static <T, R> List<R> map(T[] elements, Function<T, R> function) {
        return C.toStream(elements).map(function).collect(Collectors.toList());
    }
    
    /**
     * @see C#map(Object[], Function)
     */
    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> function) {
        return C.toStream(collection).map(function).collect(Collectors.toList());
    }
}

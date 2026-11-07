package lab_futures.examples;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Various small utils.
 *
 * awaitAll/awaitAny provide nicer versions of CompletableFuture.allOf/anyOf for handling multiple
 * futures of the same type (working around type erasure in Java arrays of generics).
 */
public class Utils {
    /** Turns a list of futures into a future of a list. */
    public static <T> CompletableFuture<List<T>> awaitAll(List<CompletableFuture<T>> futures) {
        // Copy the list to avoid using different futures if it changes.
        List<CompletableFuture<T>> futuresCopy = new ArrayList<>(futures);
        return CompletableFuture
            .allOf(futuresCopy.toArray(new CompletableFuture[0]))
            .thenApply(ignored ->
                futuresCopy.stream()
                    .map(CompletableFuture::join)
                    .toList()
            );
    }

    /** Turns a stream of futures into a future of a list. */
    public static <T> CompletableFuture<List<T>> awaitAll(Stream<CompletableFuture<T>> futures) {
        return awaitAll(futures.toList());
    }

    /** Turns any number of given futures (of the same type) into a future of a list. */
    @SafeVarargs
    public static <T> CompletableFuture<List<T>> awaitAll(CompletableFuture<T>... futures) {
        // Copy the array to avoid using different futures if it changes.
        CompletableFuture<T>[] futuresCopy = Arrays.copyOf(futures, futures.length);
        return CompletableFuture
            .allOf(futuresCopy)
            .thenApply(ignored ->
                Arrays.stream(futuresCopy)
                    .map(CompletableFuture::join)
                    .toList());
    }

    /** Turn a list of futures into a future with the first completed result. */
    @SuppressWarnings("unchecked")
    public static <T> CompletableFuture<T> awaitAny(List<CompletableFuture<T>> futures) {
        return CompletableFuture
            .anyOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(o -> (T) o);
    }

    /** Turn a stream of futures into a future with the first completed result. */
    public static <T> CompletableFuture<T> awaitAny(Stream<CompletableFuture<T>> futures) {
        return awaitAny(futures.toList());
    }

    /** Turn any number of given futures (of the same type) into a future with the first completed result. */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> CompletableFuture<T> awaitAny(CompletableFuture<T>... futures) {
        return CompletableFuture
            .anyOf(futures)
            .thenApply(o -> (T) o);
    }

    public static <K, V> Map<K, V> createMapFromEntries(List<? extends Entry<K, V>> entries) {
        return entries.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    public static void log(String message) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + message);
    }

    public static class Stopwatch {
        private Instant start;

        public void start() {
            start = Instant.now();
        }

        public Duration stop() {
            Duration duration = Duration.between(start, Instant.now());
            start = null;
            return duration;
        }

        public <T> T runWithStopwatch(Callable<T> callable) {
            try {
                start();
                T result = callable.call();
                Duration duration = stop();
                System.out.println("It took " + duration.toMillis() / 1000.0 + " s");
                return result;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

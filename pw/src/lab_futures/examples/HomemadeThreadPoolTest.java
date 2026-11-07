package lab_futures.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

import lab_futures.examples.HomemadeThreadPool.Future;
import lab_futures.examples.Utils;

public class HomemadeThreadPoolTest {

    private static final int WORK_SIZE = 100000;
    private static final int THREADS_COUNT = 10;

    public static Integer computation(int i) {
        return (i * i) % 100001;
    }

    public static void main(String[] args) {
        Utils.Stopwatch stopwatch = new Utils.Stopwatch();

        System.out.println("Individual threads:");
        long sumFromThreads = stopwatch.runWithStopwatch(() -> {
            LongAdder counter = new LongAdder();
            List<Thread> threads = new ArrayList<>();

            for (int i = 0; i < WORK_SIZE; i++) {
                final int final_i = i;
                Thread thread = new Thread(() -> {
                    counter.add(computation(final_i));
                });
                threads.add(thread);
                thread.start();
            }

            for (Thread thread : threads)
                thread.join();

            return counter.sum();
        });

        System.out.println("ThreadPool and futures:");
        long sumFromFutures = stopwatch.runWithStopwatch(() -> {
            long result = 0;

            try (var pool = new HomemadeThreadPool(THREADS_COUNT)) {

                List<Future<Integer>> futures = new ArrayList<>();

                for (int i = 0; i < WORK_SIZE; i++) {
                    final int final_i = i;
                    Future<Integer> future = pool.submit(() -> {
                        return computation(final_i);
                    });
                    futures.add(future);
                }

                for (Future<Integer> future : futures)
                    result += future.get();
            }

            return result;
        });

        if (sumFromFutures != sumFromThreads)
            throw new AssertionError();

        System.out.println("OK");
    }

}

package lab_futures.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PoolExample {
    private static final int N_TASKS = 100;
    private static final int N_WORKERS = 8;

    public static void main(String[] args) {
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (int i = 1; i <= N_TASKS; ++i) {
            final int i_final = i;
            Callable<Integer> task = () -> { return i_final * i_final; };
            tasks.add(task);
        }

        try (ExecutorService pool = Executors.newFixedThreadPool(N_WORKERS)) {

            List<Future<Integer>> futures = new ArrayList<>();
            for (Callable<Integer> task : tasks)
                futures.add(pool.submit(task));

            // Or: List<Future<Integer>> futures = pool.invokeAll(calculations);

            int sum = 0;
            for (Future<Integer> future : futures)
                sum += future.get();

            int expectedSum = N_TASKS * (N_TASKS + 1) * (2 * N_TASKS + 1) / 6;
            System.out.println((sum == expectedSum) + ": " + sum + " " + expectedSum);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

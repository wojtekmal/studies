package lab_futures.assignments;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletableFuture;

import lab_futures.examples.Utils;

public class MatrixRowSumsPooled {
    private static final int N_ROWS = 4;
    private static final int N_COLUMNS = 100;
    private static final int N_THREADS = 8;
    private static final int SLEEP_MS = 2;

    private static Integer compute(int previousSum, int columnNum) {
        try {
            Thread.sleep(SLEEP_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (previousSum + 1) * ((2 * columnNum + 1) % 4 - 2) * (2 * columnNum + 1);
    }

    private static Integer sumAndPrint(List<Integer> row, int rowNum) {
        int sum = row.stream().mapToInt(Integer::intValue).sum();
        System.out.println(rowNum + " -> " + sum);
        return sum;
    }

    private static Integer printRowSumsSequentially() {
        int previousSum = 0;
        for (int r = 0; r < N_ROWS; ++r) {
            ArrayList<Integer> row = new ArrayList<>(N_COLUMNS);
            for (int c = 0; c < N_COLUMNS; ++c) {
                row.add(compute(previousSum, c));
            }
            previousSum = sumAndPrint(row, r);
        }
        return previousSum;
    }


    public static Integer printRowSumsInParallel() throws InterruptedException, ExecutionException {
        try (ExecutorService pool = Executors.newFixedThreadPool(N_THREADS)) {

            // TODO
            throw new RuntimeException("not implemented");
        }
    }

    public static void main(String[] args) {
        List<String> startingThreads = Thread.getAllStackTraces().keySet().stream().map(Thread::getName).toList();
        Utils.Stopwatch stopwatch = new Utils.Stopwatch();

        System.out.println("-- Sequentially --");
        Integer r1 = stopwatch.runWithStopwatch(MatrixRowSumsPooled::printRowSumsSequentially);
        System.out.println("-- In a FixedThreadPool --");
        Integer r2 = stopwatch.runWithStopwatch(MatrixRowSumsPooled::printRowSumsInParallel);
        if (!r1.equals(r2))
            System.err.println("Results differ: " + r1 + " vs " + r2);
        else
            System.out.println("-- Results OK --");

        // Show all left-over threads:
        Thread.getAllStackTraces().keySet().stream().map(Thread::getName)
                .filter(name -> !startingThreads.contains(name)).forEach(System.out::println);
    }
}

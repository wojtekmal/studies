package lab_futures.assignments;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

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

    private static int[] rowSums = new int[N_ROWS];

    public static class FirstRowHelper implements Supplier<Integer>
    {
        private final int column;

        public FirstRowHelper(int column) {
            this.column = column;
        }

        @Override
        public Integer get()
        {
            return compute(0, column);
        }
    }

    public static class NextRowHelper implements Function<Integer,CompletableFuture<Integer>>
    {
        private final int column;

        public NextRowHelper(int column) {
            this.column = column;
        }

        @Override
        public CompletableFuture<Integer> apply(Integer previousSum)
        {
            return CompletableFuture.supplyAsync(() -> {return compute(previousSum, column);});
        }
    }

    public static class SummationHelper implements Function<List<Integer>,CompletableFuture<Integer>>
    {
        @Override
        public CompletableFuture<Integer> apply(List<Integer> vals)
        {
            return CompletableFuture.supplyAsync(() -> {return vals.stream().mapToInt(i -> i).sum();});
        }
    }

    public static Integer printRowSumsInParallel() throws InterruptedException, ExecutionException {
        try (ExecutorService pool = Executors.newFixedThreadPool(N_THREADS)) {

            // TODO
            // Niestety zabrakło mi czasu.
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

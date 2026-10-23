package lab04.assignments;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.function.IntBinaryOperator;

public class MatrixRowSums {
    private static final int N_ROWS = 10;
    private static final int N_COLUMNS = 100;

    private static final ConcurrentHashMap<Integer, Integer> row_sums =
        new ConcurrentHashMap<>();
    
    private static final ConcurrentHashMap<Integer, Integer> summand_counts =
        new ConcurrentHashMap<>();

    private static IntBinaryOperator matrixDefinition = (row, col) -> {
        int a = 2 * col + 1;
        return (row + 1) * (a % 4 - 2) * a;
    };

    private static void printRowSumsSequentially() {
        for (int r = 0; r < N_ROWS; ++r) {
            int sum = 0;
            for (int c = 0; c < N_COLUMNS; ++c) {
                sum += matrixDefinition.applyAsInt(r, c);
            }
            System.out.println(r + " -> " + sum);
        }
    }

    private static void printRowSumsInParallel() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int c = 0; c < N_COLUMNS; ++c) {
            final int myColumn = c;
            threads.add(new Thread(() -> {
                for (int row = 0; row < N_ROWS; row++) {
                    final int myRow = row;

                    row_sums.putIfAbsent(myRow, 0);

                    row_sums.computeIfPresent(myRow, (key, val) -> 
                        (val + matrixDefinition.applyAsInt(myRow, myColumn)));

                    summand_counts.putIfAbsent(myRow, 0);
                    summand_counts.computeIfPresent(myRow, (key, val) -> (val + 1));

                    if (summand_count == N_ROWS) {
                        int sum = row_sums.get(myRow);
                        System.out.println(myRow + " -> " + sum);

                        summand_counts.remove(myRow);
                        row_sums.remove(myRow);
                    }
                }
            }));
        }
        for (Thread t : threads) {
            t.start();
        }

        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            for (Thread t : threads) {
                t.interrupt();
            }
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("-- Sequentially --");
            printRowSumsSequentially();
            System.out.println("-- In parallel --");
            printRowSumsInParallel();
            System.out.println("-- End --");
        } catch (InterruptedException e) {
            System.err.println("Main interrupted.");
        }
    }
}

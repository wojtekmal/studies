package lab03.assignments;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.IntBinaryOperator;

public class VectorStream {
    private static final int STREAM_LENGTH = 10;
    private static final int VECTOR_LENGTH = 100;

    /**
     * Function that defines how vectors are computed: the i-th element depends on
     * the previous sum and the index i.
     * The sum of elements in the previous vector is initially given as zero.
     */
    private final static IntBinaryOperator vectorDefinition = (previousSum, i) -> {
        int a = 2 * i + 1;
        return (previousSum / VECTOR_LENGTH + 1) * (a % 4 - 2) * a + 1;
    };

    private static int[] computeVectorStreamSequentially() {
        int[] vector = new int[VECTOR_LENGTH];
        int sum = 0;
        for (int vectorNo = 0; vectorNo < STREAM_LENGTH; ++vectorNo) {
            for (int i = 0; i < VECTOR_LENGTH; ++i) {
                vector[i] = vectorDefinition.applyAsInt(sum, i);
            }
            sum = 0;
            for (int x : vector) {
                sum += x;
            }
            System.out.println(vectorNo + " -> " + sum);
        }

        return vector;
    }

    private static final CyclicBarrier barrier = 
            new CyclicBarrier(VECTOR_LENGTH, VectorStream::calculateSum);
    private static final int[] vector = new int[VECTOR_LENGTH];
    private static int parallelSum = 0;

    private static void calculateSum() {
        parallelSum = 0;

        for (int i = 0; i < VECTOR_LENGTH; i++) parallelSum += vector[i];
    }

    private static class PositionCalculator implements Runnable {
        private final int pos;

        public PositionCalculator(int pos) {
            this.pos = pos;
        }

        public void run() {
            try {
                for (int vectorIndex = 0; vectorIndex < STREAM_LENGTH; vectorIndex++) {
                    vector[pos] = vectorDefinition.applyAsInt(parallelSum, pos);
                    barrier.await();
                }
            }
            catch (InterruptedException | BrokenBarrierException e) {
                System.err.println("Interrupted: " + Thread.currentThread().getName());
            }
        }
    }

    private static void computeVectorStreamInParallel() throws InterruptedException {
        Thread[] threads = new Thread[VECTOR_LENGTH];

        for (int i = 0; i < VECTOR_LENGTH; i++) {
            threads[i] = new Thread(new PositionCalculator(i));
        }

        for (int i = 0; i < VECTOR_LENGTH; i++) {
            threads[i].start();
        }

        try {
            for (int i = 0; i < VECTOR_LENGTH; i++) {
                threads[i].join();
            }
        }
        catch (InterruptedException e) {
            System.err.println("Main thread interrupted.");
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("-- Sequentially --");
            int[] sequentialResult = computeVectorStreamSequentially();
            System.out.println("-- Parallel --");
            computeVectorStreamInParallel();
            System.out.println("-- End --");

            if (Arrays.equals(sequentialResult, vector)) {
                System.out.println("Everything OK.");
            }
            else {
                System.out.println("Results not equal.");
            }
        } catch (InterruptedException e) {
            System.err.println("Main interrupted.");
        }
    }
}

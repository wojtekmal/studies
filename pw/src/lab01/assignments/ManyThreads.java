package lab01.assignments;

import java.util.concurrent.ThreadLocalRandom;

public class ManyThreads {
    private static final int THREAD_COUNT = 5; // If it works for 5, you can increase it to 100.

    private static class MyRunnable implements Runnable {
        private final int nSubthreads;

        public MyRunnable(int nSubthreads) {
            this.nSubthreads = nSubthreads;
        }

        @Override
        public void run() {
            // FIXME: implement

            System.out.println(nSubthreads);
        }
    }

    public static void main(String[] args) {
        // FIXME: implement
    }
}

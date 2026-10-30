package lab01.assignments;

public class ManyThreads {
    private static final int THREAD_COUNT = 5; // If it works for 5, you can increase it to 100.

    private static class MyRunnable implements Runnable {
        private final int nSubthreads;

        public MyRunnable(int nSubthreads) {
            this.nSubthreads = nSubthreads;
        }

        @Override
        public void run() {
            if (nSubthreads > 1) {
                MyRunnable child_runnable = new MyRunnable(nSubthreads - 1);
                Thread child_thread = new Thread(child_runnable);
                child_thread.start();
            }

            int a = 1;
            //while (a != 0) a = (int)(ThreadLocalRandom.current().nextDouble(1.0) * 1000000);
            while (a != 0) a = (int)(Math.random() * 1000000);

            System.out.println(nSubthreads);
        }
    }

    public static void main(String[] args) {
        Thread first_thread = new Thread(new MyRunnable(THREAD_COUNT));
        first_thread.start();
    }
}

package lab_futures.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

public class HomemadeThreadPool implements AutoCloseable {

    /** An object that allows to wait for a future result. */
    public static class Future<T> {
        // Single-element queue used to notify us when the computation is done.
        private final BlockingQueue<T> channel;

        private T result = null;

        public Future(BlockingQueue<T> channel) {
            this.channel = channel;
        }

        public T get() throws InterruptedException {
            if (result == null)
                result = channel.take();
            return result;
        }
    }

    /** An object that allows to set the result of a computation later. */
    public static class Promise<T> {
        private final BlockingQueue<T> channel; // Single-element queue to put the result in.

        public Promise() {
            this.channel = new LinkedBlockingQueue<>(1);
        }

        /** Create an associated future that can be used to wait for the result. */
        public Future<T> getFuture() {
            return new Future<>(channel);
        }

        /**
         * Fullfil the promise: set the result value and wake up any threads waiting on
         * associated futures.
         */
        public void setResult(T value) throws InterruptedException {
            channel.put(value);
        }
    }

    /** Stores the task to be done and the promise used to notify when it's done. */
    private static record PromisedTask<T>(Callable<T> computation, Promise<T> promise) {
    }

    private final BlockingQueue<PromisedTask<Object>> taskQueue;
    private final List<Thread> threads; // Worker threads in the pool.


    private class Worker implements Runnable {
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    PromisedTask<Object> task = taskQueue.take();
                    Object result = task.computation().call();
                    task.promise().setResult(result);
                }
            } catch (InterruptedException e) {
                // Worker finishes.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public HomemadeThreadPool(int size) {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.threads = new ArrayList<>();
        for (int i = 0; i < size; i++)
            threads.add(new Thread(new Worker(), "Worker-" + i));
        for (Thread thread : threads)
            thread.start();
    }


    /** Submits a computation to be computed by the pool. */
    @SuppressWarnings("unchecked")
    public <T> Future<T> submit(Callable<T> computation) {
        Promise<T> promise = new Promise<>();
        try {
            taskQueue.put((PromisedTask<Object>) new PromisedTask<>(computation, promise));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return promise.getFuture();
    }

    public void close() {
        for (Thread thread : threads)
            thread.interrupt();

        try {
            for (Thread thread : threads)
                thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.threads.clear();
    }
}

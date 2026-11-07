package lab_futures.examples;

import static lab_futures.examples.Utils.log;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("unused")
public class RabbitAndTurtle {
    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            log("Interrupted while sleeping");
        }
    }

    private static CompletableFuture<String> run(String name, int seconds) {
        return CompletableFuture.supplyAsync(() -> {
            log(name + " started running");
            sleep(seconds);
            log(name + " finished running");
            return name;
        });
    }

    private static void announceWinner(String winner) {
        log("And the winner is..." + winner);
    }

    public static void main(String[] args) {
        CompletableFuture<String> rabbit = run("Rabbit", 1);
        CompletableFuture<String> turtle = run("Turtle", 5);
        CompletableFuture<Void> future = Utils
            .awaitAny(rabbit, turtle)
            .thenAccept(RabbitAndTurtle::announceWinner);

        future.join();

        // Nobody waits on the turtle task.
        // The commonPool used by CompletableFuture is terminated abruptly after the main thread finishes,
        // so the turtle task will not see an interruption, it is silently terminated.
        // You can turtle.join() or ForkJoinPool.commonPool().awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
    }
}

package lab_futures.examples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lab_futures.examples.Utils.Stopwatch;

public class BigFile {

    private static final int FILE_LENGTH = 10_000_000;
    private static final int FILES_COUNT = 30;

    /** Creates a file with random letters. */
    private static Path createOneFileSync(int id) {
        Path path = Path.of(id + ".txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {

            for (int i = 0; i < FILE_LENGTH; i++)
                writer.write(i % 50 == 0 ? '\n' : ThreadLocalRandom.current().nextInt('a', 'z' + 1));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Utils.log(path + " created!");
        return path;
    }

    /** Creates multiple files with random letters. */
    private static List<Path> createFilesSync(int count) {
        ArrayList<Path> paths = new ArrayList<>();
        for (int id = 0; id < count; id++)
            paths.add(createOneFileSync(id));
        return paths;
    }

    private static CompletableFuture<Path> createOneFileAsync(int id) {
        return CompletableFuture.supplyAsync(() -> createOneFileSync(id));
    }

    private static CompletableFuture<List<Path>> createFilesAsync(int count) {
        List<CompletableFuture<Path>> futures = IntStream.rangeClosed(1, count)
                .mapToObj(BigFile::createOneFileAsync).toList();

        return Utils.awaitAll(futures);
    }

    /** Counts the number of occurrences of a given letter in a file. */
    public static int countLetter(Path path, char letter) {
        int counter = 0;
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            for (int i = 0; i < FILE_LENGTH; i++)
                if (reader.read() == letter)
                    counter++;

        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.log("Found " + counter + " letters " + letter + " in file " + path);
        return counter;
    }

    /** Counts the total number of occurrences of a given letter in multiple files. */
    public static int countLetterSync(List<Path> paths, char letter) {
        return paths.stream()
                .map(path -> countLetter(path, letter))
                .reduce(0, Integer::sum);
    }

    public static CompletableFuture<Integer> countLetterAsync(List<Path> paths, char letter) {
        List<CompletableFuture<Integer>> futures = paths.stream()
                .map(path -> CompletableFuture.supplyAsync(() -> countLetter(path, letter)))
                .toList();

        return Utils.awaitAll(futures)
                .thenApply(partialResults -> partialResults.stream().reduce(0, Integer::sum));
    }

    public static void main(String[] args) {
        Stopwatch stopwatch = new Stopwatch();

        System.out.println("Sync version:");
        stopwatch.runWithStopwatch(() -> {
            List<Path> files = createFilesSync(FILES_COUNT);
            int count = countLetterSync(files, 'x');
            Utils.log("Found " + count + " letters 'x' in total");
            return count;
        });

        System.out.println("Async version:");
        stopwatch.runWithStopwatch(() -> {
            int count = createFilesAsync(FILES_COUNT)
                    .thenCompose(files -> countLetterAsync(files, 'x'))
                    .join();
            Utils.log("Found " + count + " letters 'x' in total");
            return count;
        });
    }

}

package pl.kat.ue.id3.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataSetReader {

    public static String[][] loadDataSet(String path, String delimiter) {
        try {
            return Files.readAllLines(Path.of(path))
                    .stream()
                    .map(line -> line.split(delimiter))
                    .toArray(String[][]::new);

        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
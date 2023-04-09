package pl.kat.ue.id3.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<String[][]> split(String[][] dataSet, Double... ratios) {
        assert 1 == java.util.Arrays.stream(ratios)
                .mapToDouble(Double::doubleValue)
                .sum();

        Arrays.shuffle(dataSet);

        List<Integer> counts = java.util.Arrays.stream(ratios)
                .map(ratio -> (int) java.lang.Math.round(dataSet.length * ratio))
                .collect(Collectors.toList());

        List<String[][]> dataSets = new ArrayList<>();
        int startInclusive = 0;

        for(int count: counts){
            String [][] set = java.util.Arrays.stream(dataSet, startInclusive, startInclusive + count)
                    .toArray(String[][]::new);
            dataSets.add(set);
            startInclusive = count;
        }

        return dataSets;
    }
}
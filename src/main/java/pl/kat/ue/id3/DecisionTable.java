package pl.kat.ue.id3;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DecisionTable {

    private final String[][] table;
    private List<Map<String, Integer>> valueFrequencies;
    private int[] uniqueValues;

    public DecisionTable(String[][] table) {
        this.table = table;
        setValueFrequencies();
        setAmountOfUniqueValues();
    }

    private void setValueFrequencies() {
        valueFrequencies = new ArrayList<>();
        int columnNumber = table[0].length;
        int rowNumber = table.length;

        for (int column = 0; column < columnNumber; column++) {
            Map<String, Integer> map = new HashMap<>();

            for (int row = 0; row < rowNumber; row++) {
                String value = table[row][column];
                map.put(value, map.getOrDefault(value, 0) + 1);
            }
            valueFrequencies.add(map);
        }
    }

    private void setAmountOfUniqueValues() {
        uniqueValues = valueFrequencies.stream()
                .mapToInt(Map::size)
                .toArray();
    }
}
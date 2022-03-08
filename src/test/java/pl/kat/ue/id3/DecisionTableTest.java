package pl.kat.ue.id3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class DecisionTableTest {

    @Test
    void shouldCalculateValueFrequencies() {
        //GIVEN
        String[][] data = new String[][]{{"old", "yes"},
                {"new", "no"},
                {"old", "no"},
                {"mid", "yes"}};

        List<Map<String, Integer>> expectedValueFrequencies = List.of(
                Map.of("old", 2, "mid", 1, "new", 1),
                Map.of("yes", 2, "no", 2)
        );

        //WHEN
        DecisionTable decisionTable = new DecisionTable(data);

        //THEN
        Assertions.assertEquals(expectedValueFrequencies, decisionTable.getValueFrequencies());
    }

    @Test
    void shouldCalculateUniqueValues() {
        //GIVEN
        String[][] data = new String[][]{{"old", "yes"},
                {"new", "no"},
                {"old", "no"},
                {"mid", "yes"}};

        int[] expectedUniqueValues = new int[]{3, 2};

        //WHEN
        DecisionTable decisionTable = new DecisionTable(data);

        //THEN
        Assertions.assertArrayEquals(expectedUniqueValues, decisionTable.getUniqueValues());
    }
}
package pl.kat.ue.id3.table;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.kat.ue.id3.utils.Math;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

class DecisionTableTest {

    @Test
    void shouldExtractDecisionClassesMetadata() {
        //given
        String[][] data = new String[][]{
                {"old", "a"},
                {"new", "b"},
                {"old", "b"},
                {"old", "a"},
                {"med", "c"},
                {"med", "a"},
        };

        //when
        Map<String, Integer> decisionsMetadata = new DecisionTable(data).getDecisionMetadata();

        //then
        Assertions.assertEquals(3, decisionsMetadata.size());
        Assertions.assertEquals(3, decisionsMetadata.get("a"));
        Assertions.assertEquals(2, decisionsMetadata.get("b"));
        Assertions.assertEquals(1, decisionsMetadata.get("c"));
    }

    @Test
    void shouldExtractAttributesMetadata() {
        //given
        String[][] data = new String[][]{
                {"old", "down"},
                {"old", "up"},
                {"med", "down"},
                {"new", "up"},
                {"new", "up"}
        };

        //when
        AttributeMetadata[] attributeMetadata = new DecisionTable(data).getAttributeMetadata();

        //then
        Assertions.assertEquals(1, attributeMetadata.length);

        AttributeMetadata firstAttribute = attributeMetadata[0];
        Assertions.assertEquals(3, firstAttribute.size());

        ValueMetadata oldMetadata = getValueMetadata(firstAttribute, "old");
        Assertions.assertEquals(Map.of("down", 1, "up", 1), oldMetadata.getDecisionMetadata());
        Assertions.assertEquals(Set.of(0, 1), oldMetadata.getIndices());

        ValueMetadata medMetadata = getValueMetadata(firstAttribute, "med");
        Assertions.assertEquals(Map.of("down", 1), medMetadata.getDecisionMetadata());
        Assertions.assertEquals(Set.of(2), medMetadata.getIndices());

        ValueMetadata newMetadata = getValueMetadata(firstAttribute, "new");
        Assertions.assertEquals(Map.of("up", 2), newMetadata.getDecisionMetadata());
        Assertions.assertEquals(Set.of(3, 4), newMetadata.getIndices());
    }

    private ValueMetadata getValueMetadata(AttributeMetadata metadata, String name) {
        return metadata.stream()
                .filter(value -> value.getValue().equals(name))
                .findFirst()
                .get();
    }

    @Test
    void shouldCalculateInfo() {
        //given
        String[][] table = prepareTable();
        DecisionTable decisionTable = new DecisionTable(table);

        //when
        double[] actual = Arrays.stream(decisionTable.getInfo())
                .map(d -> Math.round(d, 15))
                .toArray();

        //then
        Assertions.assertArrayEquals(new double[]{0.4, 0.875488750216347, 1.0}, actual);
    }

    @Test
    void shouldCalculateGain() {
        //given
        String[][] table = prepareTable();
        DecisionTable decisionTable = new DecisionTable(table);

        //when
        double[] actual = Arrays.stream(decisionTable.getGain())
                .map(d -> Math.round(d, 15))
                .toArray();

        //then
        Assertions.assertArrayEquals(new double[]{0.6, 0.124511249783653, 0}, actual);
    }

    @Test
    void shouldCalculateSplitInfo() {
        //given
        String[][] table = prepareTable();
        DecisionTable decisionTable = new DecisionTable(table);

        //when
        double[] actual = Arrays.stream(decisionTable.getSplitInfo())
                .map(d -> Math.round(d, 15))
                .toArray();

        //then
        Assertions.assertArrayEquals(new double[]{1.570950594454669, 0.970950594454669, 0.970950594454669}, actual);
    }

    @Test
    void shouldCalculateGainRatio() {
        //given
        String[][] table = prepareTable();
        DecisionTable decisionTable = new DecisionTable(table);

        //when
        double[] actual = Arrays.stream(decisionTable.getGainRatio())
                .map(d -> Math.round(d, 15))
                .toArray();

        //then
        Assertions.assertArrayEquals(new double[]{0.381934353707846, 0.128236442198776, 0}, actual);
    }

    @Test
    void shouldReturnAttributeToDivideBy() {
        //given
        String[][] table = prepareTable();
        DecisionTable decisionTable = new DecisionTable(table);

        //when
        int actual = decisionTable.extractAttributeToDivideBy();

        //then
        Assertions.assertEquals(0, actual);
    }

    @Test
    void shouldReturnMaxGainRatio() {
        //given
        String[][] table = prepareTable();
        DecisionTable decisionTable = new DecisionTable(table);

        //when
        double actual = decisionTable.getMaxGainRatio();

        //then
        Assertions.assertNotEquals(0, actual);
    }

    @Test
    void shouldReturnAllValuesOfDividingAttribute() {
        //given
        String[][] table = prepareTable();
        DecisionTable decisionTable = new DecisionTable(table);

        //when
        Set<ValueMetadata> actual = decisionTable.getValuesOfDividingAttribute();

        //then
        Assertions.assertEquals(3, actual.size());
    }

    private String[][] prepareTable() {
        return new String[][]{
                {"old", "yes", "swr", "down"},
                {"old", "no", "swr", "down"},
                {"old", "no", "hwr", "down"},
                {"med", "yes", "swr", "down"},
                {"med", "yes", "hwr", "down"},
                {"med", "no", "hwr", "up"},
                {"med", "no", "swr", "up"},
                {"new", "yes", "swr", "up"},
                {"new", "no", "hwr", "up"},
                {"new", "no", "swr", "up"},
        };
    }

    @Test
    void shouldGetMostFrequentDecisionClassWhenTableIsInconsistent() {
        //given
        String[][] table = new String[][]{
                {"old", "yes"},
                {"old", "no"},
                {"med", "no"}
        };
        DecisionTable decisionTable = new DecisionTable(table);

        //when
        String actual = decisionTable.getDecisionClass();

        //then
        Assertions.assertEquals("no", actual);
    }

    @Test
    void shouldGetSubTable() {
        //given
        String[][] table = new String[][]{
                {"old", "yes"},
                {"new", "no"},
                {"old", "no"},
                {"med", "yes"}
        };

        //when
        String[][] actualSubTable = new DecisionTable(table).getSubTable(Set.of(0, 2)).getTable();

        //then
        boolean containsRow0And2 = Arrays.stream(actualSubTable).allMatch(row ->
                Arrays.equals(row, new String[]{"old", "yes"}) || Arrays.equals(row, new String[]{"old", "no"})
        );

        Assertions.assertTrue(containsRow0And2);
        Assertions.assertEquals(2, actualSubTable.length);
    }
}
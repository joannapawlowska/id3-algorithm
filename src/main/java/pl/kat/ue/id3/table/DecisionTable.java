package pl.kat.ue.id3.table;

import lombok.Getter;
import pl.kat.ue.id3.utils.Math;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DecisionTable {

    @Getter
    private final String[][] table;
    @Getter
    private final AttributeMetadata[] attributeMetadata;
    @Getter
    private final Map<String, Integer> decisionMetadata;
    private final int decisionAttribute;
    private final int objectNumber;
    @Getter
    private int attributeToDivideBy;
    @Getter
    private double maxGainRatio;

    public DecisionTable(String[][] table) {
        this.table = table;
        this.decisionAttribute = table[0].length - 1;
        this.objectNumber = table.length;
        this.attributeMetadata = extractAttributesMetadata();
        this.decisionMetadata = extractDecisionsMetadata();
        this.attributeToDivideBy = extractAttributeToDivideBy();
    }

    public DecisionTable getSubTable(Set<Integer> indices) {
        String[][] subTable = indices.stream()
                .map(i -> table[i])
                .toArray(String[][]::new);
        return new DecisionTable(subTable);
    }

    private AttributeMetadata[] extractAttributesMetadata() {
        AttributeMetadata[] metadata = new AttributeMetadata[decisionAttribute];

        for (int attribute = 0; attribute < decisionAttribute; attribute++) {
            Map<String, ValueMetadata> attributeValues = new HashMap<>();

            for (int row = 0; row < objectNumber; row++) {
                extractValueMetadata(attributeValues, row, attribute);
            }

            metadata[attribute] = new AttributeMetadata(attributeValues.values());
        }
        return metadata;
    }

    private void extractValueMetadata(Map<String, ValueMetadata> attributeValues, int row, int attribute) {
        String value = table[row][attribute];
        String decisionClass = table[row][decisionAttribute];
        attributeValues.putIfAbsent(value, new ValueMetadata(value));
        ValueMetadata valueMetadata = attributeValues.get(value);
        valueMetadata.addIndex(row);
        valueMetadata.pointToDecisionClass(decisionClass);
    }

    private Map<String, Integer> extractDecisionsMetadata() {
        Map<String, Integer> metadata = new HashMap<>();
        for (int row = 0; row < objectNumber; row++) {
            String decisionClass = table[row][decisionAttribute];
            Integer frequency = metadata.getOrDefault(decisionClass, 0);
            metadata.put(decisionClass, frequency + 1);
        }
        return metadata;
    }

    public double[] getInfo() {
        return Arrays.stream(attributeMetadata)
                .mapToDouble(attribute ->
                        attribute.stream()
                                .mapToDouble(value -> {
                                    int frequency = value.getTotalFrequency();
                                    double probability = (double) frequency / objectNumber;
                                    return probability * Math.entropy(value.getFrequencies(), frequency);
                                }).sum()
                ).toArray();
    }

    public double[] getGain() {
        double entropyByDecisionClasses = Math.entropy(decisionMetadata.values(), objectNumber);
        return Arrays.stream(getInfo())
                .map(info -> entropyByDecisionClasses - info)
                .toArray();
    }


    public double[] getSplitInfo() {
        return Arrays.stream(attributeMetadata)
                .mapToDouble(attribute -> {
                            List<Integer> frequencies = attribute.stream()
                                    .mapToInt(ValueMetadata::getTotalFrequency)
                                    .boxed()
                                    .collect(Collectors.toList());
                            return Math.entropy(frequencies, objectNumber);
                        }
                ).toArray();
    }

    public double[] getGainRatio() {
        double[] gains = getGain();
        double[] splitInfos = getSplitInfo();
        return IntStream.range(0, decisionAttribute)
                .mapToDouble(i -> {
                    double gain = gains[i];
                    double info = splitInfos[i];
                    return gain == 0 ? 0 : gain / info;
                })
                .toArray();
    }

    public int extractAttributeToDivideBy() {
        double[] gainRatio = getGainRatio();
        attributeToDivideBy = IntStream.range(0, gainRatio.length)
                .reduce((a, b) -> gainRatio[a] < gainRatio[b] ? b : a)
                .getAsInt();
        maxGainRatio = gainRatio[attributeToDivideBy];
        return attributeToDivideBy;
    }

    public Set<ValueMetadata> getValuesOfDividingAttribute() {
        return attributeMetadata[attributeToDivideBy];
    }

    public String getDecisionClass() {
        return decisionMetadata.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }
}
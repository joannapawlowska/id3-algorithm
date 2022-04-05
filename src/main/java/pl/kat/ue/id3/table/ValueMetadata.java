package pl.kat.ue.id3.table;

import lombok.Getter;

import java.util.*;

@Getter
public class ValueMetadata {

    private final String value;
    private final Set<Integer> indices;
    private final Map<String, Integer> decisionMetadata;

    public int getTotalFrequency() {
        return indices.size();
    }

    public Collection<Integer> getFrequencies() {
        return decisionMetadata.values();
    }

    public ValueMetadata(String value) {
        this.value = value;
        this.decisionMetadata = new HashMap<>();
        this.indices = new HashSet<>();
    }

    public void addIndex(int index) {
        indices.add(index);
    }

    public void pointToDecisionClass(String decisionClass) {
        int frequency = decisionMetadata.getOrDefault(decisionClass, 0);
        decisionMetadata.put(decisionClass, frequency + 1);
    }
}
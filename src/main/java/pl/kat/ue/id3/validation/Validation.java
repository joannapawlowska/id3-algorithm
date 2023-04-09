package pl.kat.ue.id3.validation;

import pl.kat.ue.id3.tree.DecisionTree;

public interface Validation {

    void perform(String[][] testDataSet, DecisionTree decisionTree);

    double getPrecision();

    double getRecall();

    double getFMeasure();

    double getCoverage();
}

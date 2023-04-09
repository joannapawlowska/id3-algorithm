package pl.kat.ue.id3.validation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import pl.kat.ue.id3.tree.DecisionTree;

import java.util.List;


public class TrainAndTestValidation implements Validation {

    private DecisionTree decisionTree;
    private String[][] testDataSet;
    private List<String> decisionClasses;
    private int unclassified;
    @Setter(AccessLevel.PACKAGE)
    @Getter
    private double precision;
    @Setter(AccessLevel.PACKAGE)
    @Getter
    private double recall;
    @Getter
    private double fMeasure;
    @Getter
    private double coverage;
    @Setter(AccessLevel.PACKAGE)
    @Getter
    private int[][] confusionMatrix;
    /* example confusion matrix for 2 decision classes:

    target
    class
    1               |    11  10
    0               |    01  00
    -------------------------
    predicted class |    1   0

    11 - should be 1 is 1
    10 - should be 1 is 0
    01 - should be 0 is 1
    00 - should be 0 is 0
    */

    public void perform(String[][] testDataSet, DecisionTree decisionTree) {
        this.testDataSet = testDataSet;
        this.decisionTree = decisionTree;
        decisionClasses = decisionTree.getDecisionClasses();
        confusionMatrix = createConfusionMatrix();
        precision = calculatePrecision();
        recall = calculateRecall();
        fMeasure = calculateFMeasure();
        coverage = calculateCoverage();
    }

    private int[][] createConfusionMatrix() {
        int matrixSize = decisionClasses.size();
        int[][] confusionMatrix = new int[matrixSize][matrixSize];

        for (String[] object : testDataSet) {
            String output = decisionTree.predict(object);
            String target = object[object.length - 1];

            if (output != null) {
                int outputIndex = decisionClasses.indexOf(output);
                int targetIndex = decisionClasses.indexOf(target);
                confusionMatrix[targetIndex][outputIndex] += 1;
            } else {
                unclassified++;
            }
        }
        return confusionMatrix;
    }

    private static boolean isTruePositive(int j, int i) {
        return j == i;
    }

    private static boolean isFalsePositive(int j, int i) {
        return i != j;
    }

    private static boolean isFalseNegative(int j, int i) {
        return i != j;
    }

    public double calculatePrecision() {
        double precision = 0;

        for (int i = 0; i < confusionMatrix.length; i++) {
            double numerator = 0;
            double denominator = 0;
            for (int j = 0; j < confusionMatrix.length; j++) {
                if (isTruePositive(j, i)) {
                    numerator += confusionMatrix[j][i];
                } else if (isFalsePositive(j, i)) {
                    denominator += confusionMatrix[j][i];
                }
            }
            precision += numerator / (numerator + denominator);

            if (confusionMatrix.length == 2) {
                return precision;
            }
        }
        return precision / confusionMatrix.length;
    }

    public double calculateRecall() {
        double recall = 0;

        for (int i = 0; i < confusionMatrix.length; i++) {
            double numerator = 0;
            double denominator = 0;
            for (int j = 0; j < confusionMatrix.length; j++) {
                if (isTruePositive(i, j)) {
                    numerator += confusionMatrix[i][j];
                } else if (isFalseNegative(j, i)) {
                    denominator += confusionMatrix[i][j];
                }
            }
            recall += numerator / (numerator + denominator);

            if (confusionMatrix.length == 2) {
                return recall;
            }
        }
        return recall / confusionMatrix.length;
    }

    public double calculateFMeasure() {
        return 2 * precision * recall / (precision + recall);
    }

    public double calculateCoverage() {
        return ((double) testDataSet.length - unclassified) / testDataSet.length;
    }

}
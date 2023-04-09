package pl.kat.ue.id3.tree;

import lombok.Builder;
import pl.kat.ue.id3.pruning.Pruning;
import pl.kat.ue.id3.utils.DataSetReader;
import pl.kat.ue.id3.validation.Validation;

import java.util.List;

public class DecisionTreeClassifier {

    private DecisionTree decisionTree;
    private Validation validation;
    private Pruning pruning;
    private String[][] wholeDataSet;
    private String[][] trainSet;
    private String[][] testSet;
    private String[][] pruneSet;

    @Builder
    public DecisionTreeClassifier(String[][] dataSet, Pruning pruning, Validation validation,
                                  double trainSetRatio, double testSetRatio, double pruneSetRatio) {

        this.wholeDataSet = dataSet;
        this.pruning = pruning;
        this.validation = validation;
        List<String[][]> dataSets = DataSetReader.split(dataSet,
                trainSetRatio, testSetRatio, pruneSetRatio);
        this.trainSet = dataSets.get(0);
        this.testSet = dataSets.get(1);
        this.pruneSet = dataSets.get(2);
    }

    public DecisionTree construct() {
        this.decisionTree = DecisionTree.createTree(trainSet);
        System.out.println("####### FROM TRAIN SET #######");
        decisionTree.traverse();
        System.out.print("\n");


        pruning.perform(pruneSet, decisionTree);
        System.out.println("####### FROM PRUNE SET AFTER TRAIN #######");
        decisionTree.traverse();
        System.out.print("\n");

        validation.perform(testSet, decisionTree);
        System.out.println("------PRECISION " + validation.getPrecision() + "------");
        System.out.println("------RECALL " + validation.getRecall() + "------");
        System.out.println("------COVERAGE " + validation.getCoverage() + "------");
        System.out.println("------F MEASURE " + validation.getFMeasure() + "------");
        System.out.print("\n");

        this.decisionTree = DecisionTree.createTree(wholeDataSet);
        System.out.println("####### FROM WHOLE SET #######");
        decisionTree.traverse();
        System.out.print("\n");

        pruning.perform(wholeDataSet, decisionTree);
        System.out.println("####### FROM PRUNE SET AFTER TRAIN #######");
        decisionTree.traverse();
        System.out.print("\n");

        return decisionTree;
    }
}
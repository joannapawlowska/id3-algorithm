package pl.kat.ue.id3.pruning;

import lombok.Setter;
import pl.kat.ue.id3.tree.DecisionTree;
import pl.kat.ue.id3.tree.Node;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class PostPruning implements Pruning {

    @Setter
    private String[][] pruneDataSet;
    private String frequentDecisionClass;
    private DecisionTree decisionTree;

    public void perform(String[][] pruneDataSet, DecisionTree decisionTree) {
        this.decisionTree = decisionTree;
        this.pruneDataSet = pruneDataSet;
        this.frequentDecisionClass = getFrequentDecisionClass();
        traverseForPruning(decisionTree.getRoot());
    }

    private void traverseForPruning(Node parent) {
        for (Node child : parent.getChildren()) {
            if (child.isLeaf()) {
                if (meetsPruningCondition(parent)) {
                    prune(parent, frequentDecisionClass);
                    return;
                }
                break;
            }
            traverseForPruning(child);
        }
    }

    private static void prune(Node node, String decisionClass) {
        node.getChildren().clear();
        node.setLabel(decisionClass);
    }

    private boolean meetsPruningCondition(Node node) {
        double errorBeforePruning = calculateTreeError();

        List<Node> children = new ArrayList<>(node.getChildren());
        String label = node.getLabel();
        String staticDecision = node.getDecisionTable().getDecisionClass();
        prune(node, staticDecision);

        double errorAfterPruning = calculateStaticDecisionError(staticDecision);

        node.addChildren(children);
        node.setLabel(label);

        return errorAfterPruning <= errorBeforePruning + Math.sqrt(
                (errorBeforePruning * (1 - errorBeforePruning)
                        / pruneDataSet.length)
        );
    }

    public String getFrequentDecisionClass() {
        List<String> decisions = Arrays.stream(pruneDataSet)
                .map(object -> object[object.length - 1])
                .collect(Collectors.toList());

        return decisions.stream()
                .reduce(BinaryOperator.maxBy(Comparator.comparingInt(o -> Collections.frequency(decisions, o))))
                .get();
    }

    private double calculateTreeError() {
        double correctlyClassified = 0;

        for (String[] object : pruneDataSet) {
            String output = decisionTree.predict(object);
            String target = object[object.length - 1];

            if (output != null && output.equals(target)) {
                correctlyClassified++;
            }
        }
        return (pruneDataSet.length - correctlyClassified) / pruneDataSet.length;
    }

    private double calculateStaticDecisionError(String staticDecision) {
        double correctlyClassified = 0;

        for (String[] object : pruneDataSet) {
            String target = object[object.length - 1];

            if (staticDecision.equals(target)) {
                correctlyClassified++;
            }
        }
        return (pruneDataSet.length - correctlyClassified) / pruneDataSet.length;
    }
}
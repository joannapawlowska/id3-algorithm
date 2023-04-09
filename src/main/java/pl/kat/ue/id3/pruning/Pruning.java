package pl.kat.ue.id3.pruning;

import pl.kat.ue.id3.tree.DecisionTree;

public interface Pruning {

    void perform(String[][] pruneDataSet, DecisionTree decisionTree);
}
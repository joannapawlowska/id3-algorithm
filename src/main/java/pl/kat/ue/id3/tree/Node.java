package pl.kat.ue.id3.tree;

import lombok.Getter;
import lombok.Setter;
import pl.kat.ue.id3.table.DecisionTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Node {

    private String label;
    private String branchLabel;
    private List<Node> children;
    private DecisionTable decisionTable;

    public Node(String branchLabel, DecisionTable decisionTable) {
        this.branchLabel = branchLabel;
        this.decisionTable = decisionTable;
        this.children = new ArrayList<>();
    }

    public Node(String label, String branchLabel) {
        this.label = label;
        this.branchLabel = branchLabel;
        this.children = new ArrayList<>();
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public void addChildren(Node... child) {
        this.addChildren(Arrays.asList(child));
    }

    public void addChildren(List<Node> children) {
        children.forEach(this::addChild);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLabel(int label) {
        setLabel(String.valueOf(label));
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public boolean isRoot() {
        return branchLabel == null;
    }
}
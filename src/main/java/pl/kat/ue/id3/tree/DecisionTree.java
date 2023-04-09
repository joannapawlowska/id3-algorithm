package pl.kat.ue.id3.tree;

import lombok.Getter;
import pl.kat.ue.id3.table.DecisionTable;
import pl.kat.ue.id3.table.ValueMetadata;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DecisionTree {

    private final Node root;
    private List<String> decisionClasses;

    public DecisionTree(Node root) {
        this.root = root;
    }

    protected DecisionTree(Node root, List<String> decisionClasses) {
        this.root = root;
        this.decisionClasses = decisionClasses;
    }

    public static DecisionTree createTree(String[][] data) {
        DecisionTable table = new DecisionTable(data);
        Node root = new Node(null, table);
        createTree(root);
        return new DecisionTree(root, new ArrayList<>(table.getDecisionMetadata().keySet()));
    }

    private static void createTree(Node node) {
        DecisionTable table = node.getDecisionTable();

        if (table.getMaxGainRatio() == 0) {
            node.setLabel(table.getDecisionClass());
            return;
        }

        node.setLabel(table.extractAttributeToDivideBy());

        for (ValueMetadata metadata : table.getValuesOfDividingAttribute()) {
            Node child = new Node(metadata.getValue(), table.getSubTable(metadata.getIndices()));
            node.addChild(child);
            createTree(child);
        }
    }

    public void traverse() {
        traverseTree(root, "");
    }

    private static void traverseTree(Node node, String indent) {
        if (node.isLeaf() && !node.isRoot()) {
            printLeaf(node.getLabel(), node.getBranchLabel(), indent);
            return;
        } else if (node.isLeaf() && node.isRoot()) {
            printRootLeaf(node.getLabel());
        } else if (node.isRoot()) {
            printRoot(node.getLabel());
        } else {
            printNode(node.getLabel(), node.getBranchLabel(), indent);
        }
        for (Node child : node.getChildren()) {
            traverseTree(child, indent + "\t\t\t");
        }
    }

    private static void printLeaf(String label, String branchLabel, String indent) {
        System.out.printf("%s%s -> %s\n", indent, branchLabel, label);
    }

    private static void printNode(String label, String branchLabel, String indent) {
        System.out.printf("%s%s -> attribute: %s\n", indent, branchLabel, label);
    }

    private static void printRoot(String label) {
        System.out.printf("attribute: %s\n", label);
    }

    private static void printRootLeaf(String label) {
        System.out.printf("each decision -> %s\n", label);
    }

    public String predict(String[] object) {
        return traverseForPrediction(root, object);
    }

    private static String traverseForPrediction(Node node, String[] object) {
        if (node.isLeaf()) {
            return node.getLabel();
        }
        int attributeNumber = Integer.parseInt(node.getLabel());
        for (Node child : node.getChildren()) {
            if (child.getBranchLabel().equals(object[attributeNumber])) {
                return traverseForPrediction(child, object);
            }
        }
        return null;
    }
}
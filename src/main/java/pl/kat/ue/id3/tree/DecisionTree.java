package pl.kat.ue.id3.tree;

import lombok.Getter;
import pl.kat.ue.id3.table.DecisionTable;
import pl.kat.ue.id3.table.ValueMetadata;

@Getter
public class DecisionTree {

    private final Node root;

    protected DecisionTree(Node root) {
        this.root = root;
    }

    public static DecisionTree createTree(DecisionTable decisionTable) {
        Node root = new Node(null, decisionTable);
        createTree(root);
        return new DecisionTree(root);
    }

    private static void createTree(Node node) {
        DecisionTable table = node.getDecisionTable();

        if (table.isHomogenous()) {
            node.setLabel(table.getDecisionClass());
            return;
        }

        node.setLabel(table.getAttributeToDivideBy());

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
        if (node.isLeaf()) {
            printLeaf(node.getLabel(), node.getBranchLabel(), indent);
            return;
        } else if (node.isRoot()) {
            printRoot(node.getLabel());
        } else {
            printNode(node.getLabel(), node.getBranchLabel(), indent);
        }
        for (Node child : node.getChildren()) {
            traverseTree(child, indent + "\t");
        }
    }

    private static void printLeaf(String value, String branchLabel, String indent) {
        System.out.printf("%s%s -> %s\n", indent, branchLabel, value);
    }

    private static void printNode(String value, String branchLabel, String indent) {
        System.out.printf("%s%s -> a%s:\n", indent, branchLabel, value);
    }

    private static void printRoot(String value) {
        System.out.printf("a%s:\n", value);
    }
}
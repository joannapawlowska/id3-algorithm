package pl.kat.ue.id3.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.kat.ue.id3.table.DecisionTable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class DecisionTreeTest {

    private final PrintStream out = System.out;
    private final ByteArrayOutputStream outCaptor = new ByteArrayOutputStream();

    public void setUp() {
        System.setOut(new PrintStream(outCaptor));
    }

    public void tearDown() {
        System.setOut(out);
    }

    @Test
    void shouldTraverseTree() {
        //given
        setUp();
        DecisionTree tree = prepareTree();
        String expected = "a0:\n" +
                "\tnew -> up\n" +
                "\tmed -> a1:\n" +
                "\t\tyes -> down\n" +
                "\t\tno -> up\n" +
                "\told -> down\n";

        //when
        tree.traverse();

        //then
        Assertions.assertEquals(expected, outCaptor.toString());
        tearDown();
    }

    private DecisionTree prepareTree() {
        Node root = new Node("0", (String) null);

        Node child1 = new Node("up", "new");
        Node child2 = new Node("1", "med");
        Node child3 = new Node("down", "old");

        Node child2a = new Node("down", "yes");
        Node child2b = new Node("up", "no");

        root.addChildren(child1, child2, child3);
        child2.addChildren(child2a, child2b);

        return new DecisionTree(root);
    }

    @Test
    void shouldCreateTree() {
        //given
        String[][] table = prepareTable();
        DecisionTable decisionTable = new DecisionTable(table);

        //when
        DecisionTree tree = DecisionTree.createTree(decisionTable);

        //then
        Node root = tree.getRoot();
        Node child1 = getChild(root, "new");
        Node child2 = getChild(root, "med");
        Node child3 = getChild(root, "old");
        Node child2a = getChild(child2, "yes");
        Node child2b = getChild(child2, "no");

        Assertions.assertEquals("0", root.getLabel());
        Assertions.assertTrue(root.isRoot());
        Assertions.assertFalse(root.isLeaf());

        Assertions.assertEquals("up", child1.getLabel());
        Assertions.assertTrue(child1.isLeaf());

        Assertions.assertEquals("1", child2.getLabel());
        Assertions.assertFalse(child2.isLeaf());

        Assertions.assertEquals("down", child2a.getLabel());
        Assertions.assertTrue(child2a.isLeaf());

        Assertions.assertEquals("up", child2b.getLabel());
        Assertions.assertTrue(child2b.isLeaf());

        Assertions.assertEquals("down", child3.getLabel());
        Assertions.assertTrue(child3.isLeaf());
    }

    private Node getChild(Node parent, String value) {
        return parent.getChildren().stream()
                .filter(node -> node.getBranchLabel().equals(value))
                .findFirst()
                .get();
    }

    private String[][] prepareTable() {
        return new String[][]{
                {"old", "yes", "swr", "down"},
                {"old", "no", "swr", "down"},
                {"old", "no", "hwr", "down"},
                {"med", "yes", "swr", "down"},
                {"med", "yes", "hwr", "down"},
                {"med", "no", "hwr", "up"},
                {"med", "no", "swr", "up"},
                {"new", "yes", "swr", "up"},
                {"new", "no", "hwr", "up"},
                {"new", "no", "swr", "up"},
        };
    }
}
package pl.kat.ue.id3.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.kat.ue.id3.utils.DataSetReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Collectors;

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
        String expected =
                "attribute: 0\n" +
                        "\t\t\tnew -> up\n" +
                        "\t\t\tmed -> attribute: 1\n" +
                        "\t\t\t\t\t\tyes -> down\n" +
                        "\t\t\t\t\t\tno -> up\n" +
                        "\t\t\told -> down\n";

        //when
        tree.traverse();

        //then
        Assertions.assertEquals(expected, outCaptor.toString());
        tearDown();
    }

    @Test
    void shouldTraverseTreeForLargeDataSetFromFile() throws IOException {
        //given
        setUp();
        String expected = Files.readAllLines(Path.of("./src/test/resources/car_result.txt"))
                .stream()
                .map(line -> line.replace("->Atrybut", " -> attribute")
                        .replace("Atrybut", "attribute")
                        .replace("-> D:", "->")
                        .replaceAll(" {10}", "\t\t\t"))
                .collect(Collectors.joining("\n")) + "\n";

        DecisionTree tree = DecisionTree.createTree(DataSetReader.loadDataSet("./src/test/resources/car.data", ","));
        convertForProperDisplay(tree.getRoot());

        //when
        tree.traverse();

        //then
        Assertions.assertEquals(expected, outCaptor.toString());
        tearDown();
    }

    private static void convertForProperDisplay(Node node) {
        String label = node.getLabel();
        if (isInteger(label)) {
            node.setLabel(Integer.parseInt(label) + 1);
        }
        node.getChildren().sort(Comparator.comparing(Node::getBranchLabel));
        for (Node child : node.getChildren()) {
            convertForProperDisplay(child);
        }
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
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

        return new DecisionTree(root, null);
    }

    @Test
    void shouldCreateTree() {
        //given
        String[][] table = prepareTable();

        //when
        DecisionTree tree = DecisionTree.createTree(table);

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
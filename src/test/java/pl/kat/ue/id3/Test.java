package pl.kat.ue.id3;

import pl.kat.ue.id3.pruning.PostPruning;
import pl.kat.ue.id3.tree.DecisionTreeClassifier;
import pl.kat.ue.id3.utils.DataSetReader;
import pl.kat.ue.id3.validation.TrainAndTestValidation;

public class Test {

    public static void main(String[] args) {
        String[][] dataSet = DataSetReader.loadDataSet("./src/test/resources/gielda.txt", ",");

        DecisionTreeClassifier classifier = DecisionTreeClassifier.builder()
                .dataSet(dataSet)
                .trainSetRatio(0.3)
                .testSetRatio(0.5)
                .pruneSetRatio(0.2)
                .pruning(new PostPruning())
                .validation(new TrainAndTestValidation())
                .build();

        classifier.construct();
    }
}
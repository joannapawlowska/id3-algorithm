package pl.kat.ue.id3.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TrainAndTestValidationTest {


    @Test
    void should(){
        String pruned = "s";
        String trained = "";
        Assertions.assertNotEquals(pruned, trained);
    }

    @Test
    void shouldCalculatePrecisionWhen2DecisionClasses() {
        //given
        int[][] confusionMatrix = {
                {1, 3},
                {1, 15}
        };
        TrainAndTestValidation trainTestMethod = new TrainAndTestValidation();
        trainTestMethod.setConfusionMatrix(confusionMatrix);

        //when
        double actual = trainTestMethod.calculatePrecision();

        //then
        Assertions.assertEquals(0.5, actual);
    }

    @Test
    void shouldCalculateRecallWhen2DecisionClasses() {
        //given
        int[][] confusionMatrix = {
                {1, 3},
                {1, 15}
        };
        TrainAndTestValidation trainTestMethod = new TrainAndTestValidation();
        trainTestMethod.setConfusionMatrix(confusionMatrix);

        //when
        double actual = trainTestMethod.calculateRecall();

        //then
        Assertions.assertEquals(0.25, actual);
    }

    @Test
    void shouldCalculateFMeasureWhen2DecisionClasses() {
        //given
        int[][] confusionMatrix = {
                {1, 3},
                {1, 15}
        };
        TrainAndTestValidation trainTestMethod = new TrainAndTestValidation();
        trainTestMethod.setConfusionMatrix(confusionMatrix);
        trainTestMethod.setPrecision(trainTestMethod.calculatePrecision());
        trainTestMethod.setRecall(trainTestMethod.calculateRecall());

        //when
        double actual = trainTestMethod.calculateFMeasure();

        //then
        Assertions.assertEquals(0.33, Math.round(actual * 100.0) / 100.0);
    }

    @Test
    void shouldCalculatePrecisionWhen3DecisionClasses() {
        //given
        int[][] confusionMatrix = {
                {7, 1, 3},
                {8, 2, 2},
                {9, 3, 1}
        };
        TrainAndTestValidation trainTestMethod = new TrainAndTestValidation();
        trainTestMethod.setConfusionMatrix(confusionMatrix);

        //when
        double actual = trainTestMethod.calculatePrecision();

        //then
        Assertions.assertEquals(0.26, Math.round(actual * 100.0) / 100.0);
    }

    @Test
    void shouldCalculateRecallWhen3DecisionClasses() {
        //given
        int[][] confusionMatrix = {
                {7, 1, 3},
                {8, 2, 2},
                {9, 3, 1}
        };
        TrainAndTestValidation trainTestMethod = new TrainAndTestValidation();
        trainTestMethod.setConfusionMatrix(confusionMatrix);

        //when
        double actual = trainTestMethod.calculateRecall();

        //then
        Assertions.assertEquals(0.29, Math.round(actual * 100.0) / 100.0);
    }

    @Test
    void shouldCalculateFMeasureWhen3DecisionClasses() {
        //given
        int[][] confusionMatrix = {
                {7, 1, 3},
                {8, 2, 2},
                {9, 3, 1}
        };
        TrainAndTestValidation trainTestMethod = new TrainAndTestValidation();
        trainTestMethod.setConfusionMatrix(confusionMatrix);
        trainTestMethod.setPrecision(trainTestMethod.calculatePrecision());
        trainTestMethod.setRecall(trainTestMethod.calculateRecall());

        //when
        double actual = trainTestMethod.calculateFMeasure();

        //then
        Assertions.assertEquals(0.28, Math.round(actual * 100.0) / 100.0);
    }

//    @Test
//    void shouldCalculateCoverage() {
//        //given
//        TrainAndTestValidation trainTestMethod = new TrainAndTestValidation();
//
//        //when
//        double actual = trainTestMethod.calculateCoverage();
//
//        //then
//        Assertions.assertEquals(0.25, actual);
//    }

    private String[][] getDataSet() {
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
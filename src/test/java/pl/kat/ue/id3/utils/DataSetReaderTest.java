package pl.kat.ue.id3.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class DataSetReaderTest {

    @Test
    void shouldSplitDataSetInto3Subsets() {
        //given
        String[][] dataSet = getDataSet();

        //when
        List<String[][]> actual = DataSetReader.split(dataSet, 0.23, 0.27, 0.5);

        //then
        Assertions.assertEquals(2, actual.get(0).length);
        Assertions.assertEquals(3, actual.get(1).length);
        Assertions.assertEquals(5, actual.get(2).length);
    }

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
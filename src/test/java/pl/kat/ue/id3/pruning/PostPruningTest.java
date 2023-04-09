package pl.kat.ue.id3.pruning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PostPruningTest {

    @Test
    void shouldGetMostCommonDecisionClass() {
        //given
        PostPruning pruning = new PostPruning();
        pruning.setPruneDataSet(getDataSet());

        //when
        String actual = pruning.getFrequentDecisionClass();

        //then
        Assertions.assertEquals("down", actual);
    }

    private String[][] getDataSet() {
        return new String[][]{
                {"old", "yes", "swr", "down"},
                {"old", "no", "swr", "down"},
                {"old", "no", "hwr", "down"},
                {"med", "yes", "swr", "side"},
                {"med", "yes", "hwr", "down"},
                {"med", "no", "hwr", "up"},
                {"med", "no", "swr", "up"},
                {"new", "yes", "swr", "up"},
                {"new", "no", "hwr", null},
                {"new", "no", "swr", "side"},
        };
    }
}
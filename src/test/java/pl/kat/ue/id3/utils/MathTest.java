package pl.kat.ue.id3.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

class MathTest {

    @Test
    void shouldRoundHalfUp() {
        //given
        double n1 = 1205.6348;
        double n2 = 1205.6351;

        //when
        double actual1 = Math.round(n1, 2);
        double actual2 = Math.round(n2, 2);

        //then
        Assertions.assertEquals(1205.63, actual1);
        Assertions.assertEquals(1205.64, actual2);
    }

    @Test
    void shouldCalculateLog2() {
        //given
        double n = 8;

        //when
        double actual = Math.log2(n);

        //then
        Assertions.assertEquals(3, actual);
    }

    @Test
    void shouldCalculateEntropy() {
        //given
        int total = 9;
        List<Integer> list = List.of(3, 4, 2);
        DecimalFormat decimal = new DecimalFormat("#.###############");
        decimal.setRoundingMode(RoundingMode.HALF_UP);

        //when
        double actual = Math.entropy(list, total);

        //then
        Assertions.assertEquals("1,530493056757483", decimal.format(actual));
    }
}
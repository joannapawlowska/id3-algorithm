package pl.kat.ue.id3.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

class MathTest {

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
package pl.kat.ue.id3.utils;

import java.util.Collection;

public class Math {

    private Math() {
    }

    public static double log2(double x) {
        return java.lang.Math.log(x) / java.lang.Math.log(2);
    }

    public static double entropy(Collection<Integer> list, int objectsNumber) {
        return -list.stream()
                .mapToDouble(value -> {
                    double probability = (double) value / objectsNumber;
                    return probability * Math.log2(probability);
                }).sum();
    }
}
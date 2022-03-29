package pl.kat.ue.id3.utils;

import java.util.Collection;

public class Math {

    private Math() {
    }

    /**
     * Rounds given number to the specified number of decimal places, rounding half up.
     *
     * @param x             number to round.
     * @param decimalPlaces number of decimal places.
     * @return rounded number.
     */
    public static double round(double x, int decimalPlaces) {
        double multiplier = java.lang.Math.pow(10.0, decimalPlaces);
        return java.lang.Math.round(x * multiplier) / multiplier;
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
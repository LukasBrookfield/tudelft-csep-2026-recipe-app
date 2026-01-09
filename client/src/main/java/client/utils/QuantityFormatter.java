package client.utils;

import java.util.List;
import java.util.Locale;

public class QuantityFormatter {

    // Typical denominators for quantities
    private static final List<Integer> denominators = List.of(2,3,4,8);

    // "Close enough" to count as an Integer
    private static final double integerAccuracy = 1e-6;

    // If fractional part is tiny, ignore it
    private static final double smallFractionIgnore = 0.02;

    // If a fraction approximation differs by less than this, accept it
    private static final double fractionTolerance = 0.02;

    public QuantityFormatter() {
    }

    public String formatInteger(double value){
        long rounded = Math.round(value);
        return Long.toString(rounded);
    }

    public String format(double value){

        // 1. prefer integers
        if (isEssentiallyInteger(value)){
            return Long.toString(Math.round(value));
        }

        // 2. prefer "nice" fractions
        String mixed = tryMixedFraction(value);
        if (mixed != null){
            return mixed;
        }

        // 3. last option being 1 decimal
        return formatDecimal(value);
    }

    private boolean isEssentiallyInteger(double value){
        long rounded = Math.round(value);
        return Math.abs(value - rounded) < integerAccuracy;
    }

    private String tryMixedFraction(double value){
        long wholePart = (long) Math.floor(value);
        double fractionPart = value - wholePart;

        if (fractionPart < smallFractionIgnore) {
            return Long.toString(wholePart);
        }

        for (int d : denominators){
            long n = Math.round(fractionPart * d);

            // if the numerator is 0, this is not a good fit
            if (n == 0) continue;

            // if rounding increases the wholePart by one
            if (n == d) {
                wholePart += 1;
                return Long.toString(wholePart);
            }

            // check if n/d is close enough to the fractional part
            if (isAcceptableFraction(fractionPart, n, d)) {
                if (wholePart == 0) return n + "/" + d;
                return wholePart + " " + n + "/" + d;
            }
        }

        return null; // if note close to any "nice" fraction
    }

    private boolean isAcceptableFraction(double fraction, long n, int d){
        double approx = (double) n / (double) d;
        return Math.abs(fraction - approx) < fractionTolerance;
    }

    public String formatDecimal(double value){
        String s = String.format(Locale.getDefault(), "%.1f", value);

        int i = s.length() - 1;
        while (i>= 0 && s.charAt(i) == '0') i --;
        if (i >= 0 && s.charAt(i) == '.') i--;

        return s.substring(0, i + 1);
    }

    public String formatSpoon(double value){
        if (isEssentiallyInteger(value)){
            return Long.toString(Math.round(value));
        }

        return turnIntoNearestFraction(value);
    }

    public String turnIntoNearestFraction(double value){
        long wholePart = (long) Math.floor(value);
        double fractionPart = value - wholePart;

        int bestD = 0;
        int bestN = 0;
        double bestError = Double.MAX_VALUE;

        for (int d : denominators){
            long n = Math.round(fractionPart * d);

            if (n == d) {
                double error = Math.abs(fractionPart - 1.0);

                if (error < bestError){
                    bestError = error;
                    bestD = d;
                    bestN = Math.toIntExact(n);
                }
                continue;
            }

            double approximatedFraction = (double) n / (double) d;
            double error = Math.abs(fractionPart - approximatedFraction);

            if (error < bestError){
                bestError = error;
                bestD = d;
                bestN = Math.toIntExact(n);
            }
        }

        if (fractionPart < smallFractionIgnore) {
            return Long.toString(wholePart);
        }

        for (int d : denominators){
            long n = Math.round(fractionPart * d);

            // if the numerator is 0, this is not a good fit
            if (n == 0) continue;

            // if rounding increases the wholePart by one
            if (n == d) {
                wholePart += 1;
                return Long.toString(wholePart);
            }

            // check if n/d is close enough to the fractional part
            if (isAcceptableFraction(fractionPart, n, d)) {
                if (wholePart == 0) return n + "/" + d;
                return wholePart + " " + n + "/" + d;
            }
        }

        if (bestD == 0){
            return Long.toString(wholePart);
        }

        if (bestN == 0){
            return Long.toString(wholePart);
        }

        if (wholePart == 0) return bestN + "/" + bestD;
        return wholePart + " " + bestN + "/" + bestD;
    }
}

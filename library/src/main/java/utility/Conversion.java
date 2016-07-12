package utility;

/**
 * A utility class that performs conversions from data A to data B.
 */
public class Conversion {

    public static double[] toDoubleArray(String[] input) throws NumberFormatException {
        double[] output = new double[input.length];

        for (int i = 0; i < input.length; i++) {
            output[i] = Double.parseDouble(input[i]);
        }
        return output;
    }
}

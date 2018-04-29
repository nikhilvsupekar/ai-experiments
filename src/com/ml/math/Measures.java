package com.ml.math;

import java.util.Collections;
import java.util.List;

/**
 * Statistical measures used in ML algorithms
 *
 * @author Nikhil Supekar
 */
public class Measures {

    /**
     * Standard Deviation
     *
     * @param values    List of Double values to calculate the standard deviation
     * @return          standard deviation
     */
    public static double standardDeviation (List<Double> values) {
        double mean = values.stream().reduce((x, y) -> x + y).get() / values.size();
        double stdDev = 0;

        for (Double val : values) {
            stdDev += (mean - val) * (mean - val);
        }

        stdDev /= values.size();

        return Math.sqrt(stdDev);
    }

}

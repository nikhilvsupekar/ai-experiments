package com.ml.math;

import java.util.Collections;
import java.util.List;

public class Measures {

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

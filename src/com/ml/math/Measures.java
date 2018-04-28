package com.ml.math;

import java.util.Collections;
import java.util.List;

public class Measures {

    public static double standardDeviation (List<Double> values) {
        double mean = values.stream().reduce((x, y) -> x + y).get() / values.size();
        return values.stream().reduce((x, y) -> (mean - x) * (mean - x) + (mean - y) * (mean - y)).get() / values.size();
    }

}

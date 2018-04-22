package com.ml.math;

import com.ml.data.DataFrame;
import com.ml.data.DataFrameIndexOutOfBoundsException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metrics {

    public double entropy (DataFrame df, String attribute) throws DataFrameIndexOutOfBoundsException {
        List<String> attributeCol = df.getColumn(attribute);
        Map<String, Integer> targetFrequencyMap = new HashMap<>();

        // build frequency table for target column
        for (String val : attributeCol) {
            if (!targetFrequencyMap.containsKey(val)) {
                targetFrequencyMap.put(val, 1);
            } else {
                targetFrequencyMap.put(val, targetFrequencyMap.get(val) + 1);
            }
        }

        double entropyVal = 0;

        for (String val : targetFrequencyMap.keySet()) {
            int frequency = targetFrequencyMap.get(val);
            double p = frequency * 1.0 / attributeCol.size();
            entropyVal += -p * Math.log(p) / Math.log(2);
        }

        return entropyVal;
    }



    public double informationGain (DataFrame df, String attribute) {

        return 0;
    }
}

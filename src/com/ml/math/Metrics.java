package com.ml.math;

import com.ml.data.DataFrame;
import com.ml.data.DataFrameIndexOutOfBoundsException;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metrics {

    public static double entropy (DataFrame df, String attribute) throws DataFrameIndexOutOfBoundsException {
//        List<String> attributeCol = df.getColumn(attribute);
//        Map<String, Integer> targetFrequencyMap = new HashMap<>();
//
//        // build frequency table for target column
//        for (String val : attributeCol) {
//            if (!targetFrequencyMap.containsKey(val)) {
//                targetFrequencyMap.put(val, 1);
//            } else {
//                targetFrequencyMap.put(val, targetFrequencyMap.get(val) + 1);
//            }
//        }

        Map<String, Integer> targetFrequencyMap = df.table(attribute);
        double entropyVal = 0;

        for (String val : targetFrequencyMap.keySet()) {
            int frequency = targetFrequencyMap.get(val);
            double p = frequency * 1.0 / df.getColumn(attribute).size();
            entropyVal += -p * Math.log(p) / Math.log(2);
        }

        return entropyVal;
    }



    public static double informationGain (DataFrame df, String attribute, String target) throws DataFrameIndexOutOfBoundsException {

        double informationGainVal = entropy(df, target);

        for (DataFrame d : df.split(attribute)) {
            informationGainVal -= d.getNumRows() *1.0 / df.getNumRows() * entropy(d, target);
        }

        return informationGainVal;
    }
}

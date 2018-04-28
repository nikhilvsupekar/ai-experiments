package com.ml.data.processor;

import com.ml.data.DataFrame;
import com.ml.data.DataFrameIndexOutOfBoundsException;
import com.ml.math.Measures;
import javafx.util.Pair;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.stream.Collectors;

public class PreProcessor {

    DataFrame dropEmptyColumnsByThreshold (DataFrame df, double threshold) throws DataFrameIndexOutOfBoundsException {
        for (String column : df.getHeaders()) {
            if (df.getColumn(column).stream().filter(v -> v.equals("")).count() * 1.0 / df.getNumRows() * 100 > threshold) {
                df.dropColumn(column);
            }
        }

        return df;
    }

    DataFrame convertColumnToRange (DataFrame df, String column) throws DataFrameIndexOutOfBoundsException, NumberFormatException{
        List<String> colValues = df.getColumn(column);
        Map<Pair, Integer> rangeMap = buildRangeMap(colValues, 30, 5);
        List<String> convertedColValues = new ArrayList<>();

        for (int i = 0; i < colValues.size(); i++) {
            String val = colValues.get(i);
            for (Map.Entry e : rangeMap.entrySet()) {
                if (Double.parseDouble(val) >= (Double)((Pair)e.getKey()).getKey() &&
                        Double.parseDouble(val) >= (Double)((Pair)e.getKey()).getValue()) {
                    convertedColValues.add((e.getValue()).toString());
                    break;
                }
            }
        }

        df.setColumn(column, convertedColValues);

        return df;
    }

    private Map<Pair, Integer> buildRangeMap (List<String> column, Integer uniqCountThreshold, Integer numIntervals) {
        Set<Double> values = new HashSet<>();
        Map<Pair, Integer> rangeMap = new HashMap<>();

        for (String v : column) {
            values.add(Double.parseDouble(v));
        }

        Double max = Collections.max(values);
        Double min = Collections.min(values);
        Double intervalSize;

        if (values.size() >= uniqCountThreshold) {
            intervalSize = Measures.standardDeviation(new ArrayList<>(values)) / 2;
            numIntervals = (int)((max - min) / intervalSize);
        } else {
            intervalSize = (max - min) / numIntervals;
        }

        for (int i = 0; i < numIntervals; i++) {
            rangeMap.put(new Pair<>(min + intervalSize * i, min + intervalSize * (i + 1)), i);
        }

        return rangeMap;
    }
}

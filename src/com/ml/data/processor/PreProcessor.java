package com.ml.data.processor;

import com.ml.data.DataFrame;
import com.ml.data.DataFrameIndexOutOfBoundsException;
import com.ml.math.Measures;
import javafx.util.Pair;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data processing functionality such as cleaning, transformation, etc.
 *
 * @author Nikhil Supekar
 */
public class PreProcessor {

    /**
     * given a data frame and a threshold percentage, drop all columns of the data frame where
     * percentage of empty values is greater than the threshold
     *
     * @param df            a data frame
     * @param threshold     percentage value indicating the proportion of acceptable empty values
     * @return              transformed data frame with columns meeting the above condition dropped
     * @throws DataFrameIndexOutOfBoundsException
     */
    DataFrame dropEmptyColumnsByThreshold (DataFrame df, double threshold)
            throws DataFrameIndexOutOfBoundsException {
        for (String column : df.getHeaders()) {
            if (df.getColumn(column).stream().filter(v -> v.equals("")).count() * 1.0 / df.getNumRows() * 100 > threshold) {
                df.dropColumn(column);
            }
        }

        return df;
    }


    /**
     * convert a continuous column or any column taking a huge number of distinct values into ranges
     *
     * @param df        a data frame
     * @param column    column to be converted to range
     * @return          transformed data frame with the given column converted to a range
     * @throws DataFrameIndexOutOfBoundsException
     * @throws NumberFormatException    if the column contains a value that is not castable to double
     */
    public static DataFrame convertColumnToRange (DataFrame df, String column)
            throws DataFrameIndexOutOfBoundsException, NumberFormatException{
        List<String> colValues = df.getColumn(column);
        Map<Pair, Integer> rangeMap = buildRangeMap(colValues, 30, 5);
        List<String> convertedColValues = new ArrayList<>();

        for (int i = 0; i < colValues.size(); i++) {
            String val = colValues.get(i);
            boolean flag = false;
            for (Map.Entry e : rangeMap.entrySet()) {
                if (Double.parseDouble(val) >= (Double)((Pair)e.getKey()).getKey() &&
                        Double.parseDouble(val) <= (Double)((Pair)e.getKey()).getValue()) {
                    convertedColValues.add((e.getValue()).toString());
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                convertedColValues.add("0");
            }
        }

        df.setColumn(column, convertedColValues);

        return df;
    }

    /**
     * used in converting a continuous variable into ranges.
     * values are mapped to ranges based on some statistics.
     * if number of distinct values that the column takes < uniqCountThreshold
     *      then convert the column into numIntervals number of adjacent ranges
     * else
     *      use standard deviation as a measure to construct ranges
     *
     * Assumption: that the column to be transformed is castable to double.
     * If the column contains empty values, it must be cleaned first using the replaceEmptyValues API
     *
     * @param column                column to be converted
     * @param uniqCountThreshold    count at which to decide whether to use simple
     *                              numIntervals based statistics / to use standard deviation
     *                              based statistics
     * @param numIntervals          if simple statistics, then create numIntervals number of
     *                              adjacent ranges
     * @return                      a map between range and value. The value is incremental for adjacent pairs.
     *                              a pair represents the lower bound and the upper bound of a range
     */
    private static Map<Pair, Integer> buildRangeMap (List<String> column,
                                                     Integer uniqCountThreshold,
                                                     Integer numIntervals) {
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


    /**
     * given a dataframe, a column and a defaultString, replace all occurrences of empty
     * values in the column with defaultString
     *
     * @param df                a data frame
     * @param columnName        a column name
     * @param defaultString     string by which to replace all occurrences of empty values in column
     * @return                  transformed data frame
     * @throws DataFrameIndexOutOfBoundsException
     */
    public static DataFrame replaceEmptyValues (DataFrame df, String columnName, String defaultString)
            throws DataFrameIndexOutOfBoundsException {
        List<String> column = df.getColumn(columnName);

        for (int i = 0; i < column.size(); i++) {
            if (column.get(i).equals("")) {
                column.set(i, defaultString);
            }
        }

        df.setColumn(columnName, column);

        return df;
    }
}

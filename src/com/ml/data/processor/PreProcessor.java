package com.ml.data.processor;

import com.ml.data.DataFrame;
import com.ml.data.DataFrameIndexOutOfBoundsException;

public class PreProcessor {

    DataFrame dropEmptyColumnsByThreshold (DataFrame df, double threshold) throws DataFrameIndexOutOfBoundsException {
        for (String column : df.getHeaders()) {
            if (df.getColumn(column).stream().filter(v -> v.equals("")).count() / df.getNumRows() * 100 > threshold) {
                df.dropColumn(column);
            }
        }

        return df;
    }


}

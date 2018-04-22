package com.drivers;

import com.ml.data.DataFrame;
import com.ml.io.DataFrameReader;

public class DataFrameDriver {

    public static void main (String[] args) {
        DataFrame df = DataFrameReader.readDataFrame("C:\\Users\\NIKHIL-PC\\repos\\ai-experiments\\data\\titanic\\train.csv", ",");
        df.print();
    }

}

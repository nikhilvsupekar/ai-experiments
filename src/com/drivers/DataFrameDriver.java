package com.drivers;

import com.ml.data.DataFrame;
import com.ml.data.DataFrameIndexOutOfBoundsException;
import com.ml.io.DataFrameReader;

import java.util.List;

public class DataFrameDriver {

    public static void main (String[] args) throws DataFrameIndexOutOfBoundsException {
        DataFrame df = DataFrameReader.readDataFrame("C:\\Users\\NIKHIL-PC\\repos\\ai-experiments\\data\\titanic\\train.csv", ",");
//        df.print();
        List<DataFrame> l = df.split("Embarked");
        l.get(0).print();
    }

}

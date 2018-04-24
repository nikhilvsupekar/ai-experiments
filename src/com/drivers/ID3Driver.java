package com.drivers;

import com.ml.algorithms.classification.ID3;
import com.ml.data.DataFrame;
import com.ml.data.DataFrameIndexOutOfBoundsException;
import com.ml.io.DataFrameReader;
import com.structures.DecisionTreeNode;

public class ID3Driver {


    public static void main (String[] args) throws DataFrameIndexOutOfBoundsException {

        DataFrame df = DataFrameReader.readDataFrame("C:\\Users\\NIKHIL-PC\\repos\\ai-experiments\\data\\titanic\\train.csv", ",");



        DecisionTreeNode node = ID3.buildDecisionTree(df, "Survived", df.getHeaders());

        System.out.println("done");
    }
}

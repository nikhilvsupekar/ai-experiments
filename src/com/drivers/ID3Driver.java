package com.drivers;

import com.ml.algorithms.classification.ID3;
import com.ml.data.DataFrame;
import com.ml.data.DataFrameIndexOutOfBoundsException;
import com.ml.io.DataFrameReader;
import com.structures.DecisionTreeNode;

import java.util.ArrayList;
import java.util.List;

public class ID3Driver {


    public static void main (String[] args) throws DataFrameIndexOutOfBoundsException {

        DataFrame df = DataFrameReader.readDataFrame("C:\\Users\\NIKHIL-PC\\repos\\ai-experiments\\data\\titanic\\train.csv", ",");

        df.dropColumn("PassengerId");
        df.dropColumn("Name");
        df.dropColumn("Ticket");
        df.dropColumn("Cabin");


        List<String> attributes = new ArrayList<>(df.getHeaders());
        attributes.remove("Survived");
        DecisionTreeNode node = ID3.buildDecisionTree(df, "Survived", attributes);

        System.out.println("done");
    }
}

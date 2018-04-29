package com.ml.algorithms.classification;

import com.ml.data.DataFrame;
import com.ml.data.DataFrameIndexOutOfBoundsException;
import com.ml.math.Metrics;
import com.structures.DecisionTreeNode;

import java.util.*;

/**
 * A basic implementation of the Iterative Dichotomizer.
 * Used in constructing Decision Trees.
 *
 * @author  Nikhil Supekar
 */
public class ID3 {

    /**
     * recursively builds decision tree from the data frame, target column and the list of attributes
     *
     *
     * @param df            data frame representing the data to build the decision tree upon
     * @param target        target column to classify the instances upon
     * @param attributes    list of attributes to use to build the Decision Tree
     * @return              Decision Tree Node of the fully constructed decision below this level
     * @throws DataFrameIndexOutOfBoundsException
     */
    public static DecisionTreeNode buildDecisionTree (DataFrame df, String target, List<String> attributes) throws DataFrameIndexOutOfBoundsException {
        DecisionTreeNode node = new DecisionTreeNode();

        if (df.table(target).size() == 1) {
            node.setLabel(df.getColumn(target).get(0));
        } else if (attributes.size() == 0) {
            node.setLabel(getMostCommonTargetValue(df, target));
        } else {
            String bestAttribute = getBestClassifier(df, target, attributes);
            node.setAttribute(bestAttribute);
            Set<String> bestAttributeValues = df.table(bestAttribute).keySet();

            Map<String, DecisionTreeNode> children = new HashMap<>();
            for (String val : bestAttributeValues) {
                DataFrame subset = df.subset(bestAttribute, val);
                DecisionTreeNode childNode = new DecisionTreeNode();

                if (subset == null || subset.getNumRows() == 0) {
                    childNode.setLabel(getMostCommonTargetValue(df, target));
                } else {
                    List<String> t = new ArrayList<>(attributes);
                    t.remove(bestAttribute);
                    childNode = buildDecisionTree(subset, target, t);
                }

                children.put(val, childNode);
            }

            node.setChildren(children);
        }

        return node;
    }


    /**
     * given a data frame and a column, find the most common value the column takes
     *
     * @param df        data frame representing the data
     * @param target    the target column
     * @return          Value of the most commonly occurring value of the target column
     * @throws DataFrameIndexOutOfBoundsException
     */
    private static String getMostCommonTargetValue (DataFrame df, String target) throws DataFrameIndexOutOfBoundsException {
        Map<String, Integer> t = df.table(target);

        Map.Entry<String, Integer> maxEntry = null;
        for(Map.Entry<String, Integer> e : t.entrySet()) {
            if (maxEntry == null || e.getValue() > maxEntry.getValue()) {
                maxEntry = e;
            }
        }

        return maxEntry.getKey();
    }

    /**
     * get the best attribute to classify the instances at some point of building the decision tree
     *
     * @param df            data frame representing the data
     * @param target        the target column
     * @param attributes    list of column attributes to consider for finding the best classifying attribute
     * @return
     * @throws DataFrameIndexOutOfBoundsException
     */
    public static String getBestClassifier (DataFrame df, String target, List<String> attributes) throws DataFrameIndexOutOfBoundsException {
        String bestAttribute = "";
        double maxMetric = -1;

        for (String attribute : attributes) {
            if (Metrics.informationGain(df, attribute, target) > maxMetric) {
                bestAttribute = attribute;
                maxMetric = Metrics.informationGain(df, attribute, target);
            }
        }

        return bestAttribute;
    }
}

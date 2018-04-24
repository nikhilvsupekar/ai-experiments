package com.ml.algorithms.classification;

import com.ml.data.DataFrame;
import com.ml.data.DataFrameIndexOutOfBoundsException;
import com.ml.math.Metrics;
import com.structures.DecisionTreeNode;

import java.util.*;

public class ID3 {

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

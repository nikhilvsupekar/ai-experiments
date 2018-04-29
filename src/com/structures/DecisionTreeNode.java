package com.structures;

import java.util.Map;

/**
 * Represents a node in the Decision Tree built by classification algorithms
 *
 * @author Nikhil Supekar
 */
public class DecisionTreeNode {

    /**
     * the best classifying attribute at this node of the decision tree
     */
    String attribute;

    String value;

    /**
     * list of child nodes corresponding the different values of the best classifying attribute
     * the data subsets every time a split is created
     */
    Map<String, DecisionTreeNode> children;

    /**
     * if there are no children, the node must represent a target class that finally classifies the instances based of the branch of the tree
     * this happens when all instances of data have the same value for the target column
     */
    String label;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Map<String, DecisionTreeNode> getChildren() {
        return children;
    }

    public void setChildren(Map<String, DecisionTreeNode> children) {
        this.children = children;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    /**
     * Console tree printer to visualize the decision tree
     * Recursive tree printer
     *
     * @param depth     the depth from which to start printing the decision tree.
     *                  default passed from the driver should be 0 to visualize the entire tree.
     */
    public void print(int depth) {
//        for (int i = 0; i < depth; i++) {
//            System.out.print(" ");
//        }

        if (label == null || label.equals("")) {
//            System.out.println(attribute + " = " + value);

            children.forEach((k, v) -> {
                for (int i = 0; i < depth; i++) {
                    System.out.print(" ");
                }
                System.out.println(attribute + " = " + k);
                v.print(depth + 2);
            });

        } else {
            for (int i = 0; i < depth; i++) {
                System.out.print(" ");
            }
            System.out.println(label);
        }
    }
}

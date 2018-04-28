package com.structures;

import java.util.Map;

public class DecisionTreeNode {
    String attribute;
    String value;
    Map<String, DecisionTreeNode> children;
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

    public void print(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print(" ");
        }

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

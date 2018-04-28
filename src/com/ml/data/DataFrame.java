package com.ml.data;

import com.ml.io.InvalidDataFrameFormatException;

import java.util.*;
import java.util.stream.Collectors;

public class DataFrame {

    String delim;
    private List<String> headers;
    private List<List<String>> data;


    public DataFrame () {
        this.delim = ",";
        this.headers = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    public DataFrame (String delim) {
        this.delim = delim;
        this.headers = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    public DataFrame (String delim, List<List<String>> data) {
        this.delim = delim;
        this.data = data;
    }


    public String getElement (int i, int j) throws DataFrameIndexOutOfBoundsException {
        if (i >= getNumRows() || j >= getNumCols()) {
            throw new DataFrameIndexOutOfBoundsException();
        }

        return getColumn(j).get(i);
    }

    public List<String> getColumn (int i) throws DataFrameIndexOutOfBoundsException {
        if (i >= getNumCols()) {
            throw new DataFrameIndexOutOfBoundsException();
        }

        List<String> col = new ArrayList<>();

        for (int j = 0; j < getNumRows(); j++) {
            col.add(getRow(j).get(i));
        }

        return col;
    }

    public List<String> getColumn (String colName) throws DataFrameIndexOutOfBoundsException {
        int colCount = 0;
        int colIndex = -1;

        for (String col : headers) {
            if (col.equals(colName)) {
                colIndex = colCount;
                break;
            }

            colCount++;
        }

        if (colIndex == -1) {
            return null;
        }

        return getColumn(colIndex);
    }

    public List<String> getRow (int i) throws DataFrameIndexOutOfBoundsException {
        if (i >= getNumRows()) {
            throw new DataFrameIndexOutOfBoundsException();
        }

        return data.get(i);
    }


    public void dropRow (int i) throws DataFrameIndexOutOfBoundsException {
        if (i >= getNumRows()) {
            throw new DataFrameIndexOutOfBoundsException();
        }

        data.remove(i);
    }

    public void dropColumn (int i) throws DataFrameIndexOutOfBoundsException {
        if (i >= getNumCols()) {
            throw new DataFrameIndexOutOfBoundsException();
        }

        headers.remove(i);

        for (int j = 0; j < getNumRows(); j++) {
            data.get(j).remove(i);
        }
    }

    public void dropColumn (String colName) throws DataFrameIndexOutOfBoundsException {
        dropColumn(getColumnIndex(colName));
    }

    public void addColumn (String colName, List<String> colValues) throws InvalidDataFrameFormatException {

        if (colValues.size() != getNumRows()) {
            throw new InvalidDataFrameFormatException();
        }

        headers.add(colName);

        for (int i = 0; i < getNumRows(); i++) {
            data.get(i).add(colValues.get(i));
        }
    }


    public int getNumCols () {
        return this.headers.size();
    }

    public int getNumRows () {
        return this.data.size();
    }


    public void addRow(List<String> row) {
        data.add(row);
    }


    // useful if used with Jshell
    public void print() {

        System.out.println(headers);

        for (int i = 0; i < getNumRows(); i++) {
//            for (int j = 0; j < getNumCols(); j++) {
//                System.out.println(data.get(i).get(j));
//            }

            System.out.println(data.get(i));
        }

    }


    public List<DataFrame> split (String attribute) throws DataFrameIndexOutOfBoundsException {
        if (!headers.contains(attribute)) {
            throw new DataFrameIndexOutOfBoundsException();
        }

//        Map<String, List<Integer>> splitMap = new TreeMap<>();
//
//        List<String> attributeCol = this.getColumn(attribute);
//        for (int i = 0; i < attributeCol.size(); i++) {
//            if (!splitMap.containsKey(attributeCol.get(i))) {
//                splitMap.put(attributeCol.get(i), new ArrayList<>(i));
//            } else {
//                List<Integer> rowList = splitMap.get(attributeCol.get(i));
//                rowList.add(i);
//                splitMap.put(attributeCol.get(i), rowList);
//            }
//        }


        List<DataFrame> splitList = new ArrayList<>();

        for (String val : new HashSet<>(this.getColumn(attribute))) {
            DataFrame d = new DataFrame(this.delim, this.getData().stream().filter(row -> row.get(getColumnIndex(attribute)).equals(val)).collect(Collectors.toList()));
            d.setHeaders(headers);
            splitList.add(d);
        }

        return splitList;
    }


    public DataFrame subset (String attribute, String value) throws DataFrameIndexOutOfBoundsException {
        for (DataFrame splitFrame : split(attribute)) {
            if (splitFrame.getColumn(attribute).get(0).equals(value)) {
                return splitFrame;
            }
        }

        return null;
    }

    public int getColumnIndex(String colName) {
        return headers.indexOf(colName);
    }


    public Map<String, Integer> table (String attribute) throws DataFrameIndexOutOfBoundsException {
        Map<String, Integer> tableMap = new TreeMap<>();

        this.getColumn(attribute).forEach(v -> {
            if (!tableMap.containsKey(v)) {
                tableMap.put(v, 1);
            } else {
                tableMap.put(v, tableMap.get(v) + 1);
            }
        });

        return tableMap;
    }


    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}

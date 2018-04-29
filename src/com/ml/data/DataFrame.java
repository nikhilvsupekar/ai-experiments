package com.ml.data;

import com.ml.io.InvalidDataFrameFormatException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A row based implementation of the data frame.
 * Mostly done for understanding what it takes to build a ML platform.
 *
 *
 * @author  Nikhil Supekar
 */
public class DataFrame {

    private String delim;

    /**
     * ordered-list of column names
     * separate variables for headers and data needs extra care when updating/dropping columns
     * if column is dropped from data, it must also be dropped from headers explicitly
     */
    private List<String> headers;

    /**
     * row-based impl for data frame
     * list of rows
     * each row = list of column values
     */
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

    /**
     * get the element from the i-th row and j-th column of the data frame
     *
     *
     * @param i     row index
     * @param j     column index
     * @return      value of the element at i-th row and j-th column
     * @throws DataFrameIndexOutOfBoundsException
     */
    public String getElement (int i, int j) throws DataFrameIndexOutOfBoundsException {
        if (i >= getNumRows() || j >= getNumCols()) {
            throw new DataFrameIndexOutOfBoundsException();
        }

        return getColumn(j).get(i);
    }

    // get column by index

    /**
     * get the i-th column of the data frame
     *
     * @param i     column index
     * @return      i-th column of the data frame represented by a List
     * @throws DataFrameIndexOutOfBoundsException
     */
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

    /**
     * get a column from the data frame by name
     * the column name must be present in the list of headers
     *
     * @param colName   column name
     * @return          column corresponding to the header in the data frame
     * @throws DataFrameIndexOutOfBoundsException
     */
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

    /**
     * get the i-th row of the data frame
     *
     * @param i     row index
     * @return      i-th row of the data frame
     * @throws DataFrameIndexOutOfBoundsException
     */
    public List<String> getRow (int i) throws DataFrameIndexOutOfBoundsException {
        if (i >= getNumRows()) {
            throw new DataFrameIndexOutOfBoundsException();
        }

        return data.get(i);
    }


    /**
     * given column name and a list of values, update the column with the list of values provided
     * the column must be present in the list of headers
     *
     * @param colName       name of the column to set
     * @param colValues     list of values to be set for the column
     * @throws DataFrameIndexOutOfBoundsException
     */
    public void setColumn (String colName, List<String> colValues) throws DataFrameIndexOutOfBoundsException {
        int colIndex = getColumnIndex(colName);

        for (int i = 0; i < getNumRows(); i++) {
            getRow(i).set(colIndex, colValues.get(i));
        }
    }


    /**
     * drop the i-th row of the data frame
     *
     * @param i     row index
     * @throws DataFrameIndexOutOfBoundsException
     */
    public void dropRow (int i) throws DataFrameIndexOutOfBoundsException {
        if (i >= getNumRows()) {
            throw new DataFrameIndexOutOfBoundsException();
        }

        data.remove(i);
    }


    /**
     * drop the i-th column of data frame
     *
     * @param i     column index
     * @throws DataFrameIndexOutOfBoundsException
     */
    public void dropColumn (int i) throws DataFrameIndexOutOfBoundsException {
        if (i >= getNumCols()) {
            throw new DataFrameIndexOutOfBoundsException();
        }

        headers.remove(i);

        for (int j = 0; j < getNumRows(); j++) {
            data.get(j).remove(i);
        }
    }


    /**
     * drop column by name
     * the name must be present in the list of headers
     *
     * @param colName   column name
     * @throws DataFrameIndexOutOfBoundsException
     */
    public void dropColumn (String colName) throws DataFrameIndexOutOfBoundsException {
        dropColumn(getColumnIndex(colName));
    }


    /**
     * add column to the data frame
     * column will be added at the end of list of headers
     *
     * @param colName       name of the new column to be added
     * @param colValues     list of values of the new column
     * @throws InvalidDataFrameFormatException  if number of records in the list of values passed doesn't match with the number of records in the data frame
     */
    public void addColumn (String colName, List<String> colValues) throws InvalidDataFrameFormatException {

        if (colValues.size() != getNumRows()) {
            throw new InvalidDataFrameFormatException();
        }

        headers.add(colName);

        for (int i = 0; i < getNumRows(); i++) {
            data.get(i).add(colValues.get(i));
        }
    }


    /**
     * get number of rows in the data frame
     *
     * @return  number of rows
     */
    public int getNumCols () {
        return this.headers.size();
    }


    /**
     * get number of columns in the data frame
     *
     * @return  number of columns
     */
    public int getNumRows () {
        return this.data.size();
    }


    /**
     * add a row to the data frame
     *
     * @param row   list of values of the row to add to the data frame
     * @throws InvalidDataFrameFormatException if the number of values in the new row doesn't match with number of columns of the data frame
     */
    public void addRow(List<String> row) throws InvalidDataFrameFormatException {

        if (row.size() != headers.size()) {
            throw new InvalidDataFrameFormatException();
        }

        data.add(row);
    }


    /**
     * print the values of the data frame
     * useful if the program is run interactively via JShell
     */
    public void print() {

        System.out.println(headers);

        for (int i = 0; i < getNumRows(); i++) {
//            for (int j = 0; j < getNumCols(); j++) {
//                System.out.println(data.get(i).get(j));
//            }

            System.out.println(data.get(i));
        }

    }


    /**
     * split the data frame by an attribute
     * splitting results in the creation of a list of data frames
     * each data frame in the list has the same value for the attribute by which is was split
     *
     * @param attribute column name by which the data frame has to be split
     * @return          list of data frames where each data frame has the same value for column = attribute
     * @throws DataFrameIndexOutOfBoundsException
     */
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


    /**
     * given a column name and a value, returns a new data frame where all rows have value of column attribute = value
     * can be optimized by dropping the usage of the split frame functionality
     *
     * @param attribute column name
     * @param value     value of the column
     * @return          a new data frame where each row has attribute = value
     * @throws DataFrameIndexOutOfBoundsException
     */
    public DataFrame subset (String attribute, String value) throws DataFrameIndexOutOfBoundsException {
        for (DataFrame splitFrame : split(attribute)) {
            if (splitFrame.getColumn(attribute).get(0).equals(value)) {
                return splitFrame;
            }
        }

        return null;
    }


    /**
     * given column name, get the column index
     *
     *
     * @param colName   column name
     * @return          index of the column
     */
    public int getColumnIndex(String colName) {
        return headers.indexOf(colName);
    }


    /**
     * creates a table for a given attribute
     * table gives the frequency of each value of the attribute
     *
     * @param attribute column name
     * @return          a map structure which gives frequency for every distinct value of the attribute
     * @throws DataFrameIndexOutOfBoundsException
     */
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

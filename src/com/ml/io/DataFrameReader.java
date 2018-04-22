package com.ml.io;


import com.ml.data.DataFrame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataFrameReader {

    public static DataFrame readDataFrame (String filePath, String delim) {
        String rawDelim = delim;
        delim += "(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)";

        DataFrame df = new DataFrame(delim);
        int colCount = 0;
        int rowCount = 0;
        List<String> headers = new ArrayList<>();
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(filePath));

            String line;

            while ((line = br.readLine()) != null) {

                if (rowCount == 0) {
                    if (!line.contains(rawDelim)) {
                        colCount = 1;
                        headers.add(line);
                    } else {
                        String[] lineSplit = line.split(delim, -1);
                        colCount = lineSplit.length;

                        Collections.addAll(headers, lineSplit);
                        df.setHeaders(headers);
                    }

                } else {
                    if (colCount != 0 && colCount != line.split(delim, -1).length) {
                        throw new InvalidDataFrameFormatException();
                    }

                    List<String> row = new ArrayList<>();
                    Collections.addAll(row, line.split(delim, -1));
                    df.addRow(row);
                }

                rowCount++;
            }
        } catch (IOException | InvalidDataFrameFormatException e) {
            e.printStackTrace();
        }

        return df;
    }

}

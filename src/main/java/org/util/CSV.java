package org.util;

import java.util.Arrays;
import java.util.List;

public class CSV {

    public static String buildLineCSV(String... ls){
        StringBuilder csvLine = new StringBuilder(new String());
        List<String> stringList = Arrays.asList(ls);
        for(String str: stringList){
            csvLine.append(str).append(", ");
        }
        csvLine.setLength(csvLine.length() - 2);
        csvLine.append("\n");
        return csvLine.toString();
    }
}

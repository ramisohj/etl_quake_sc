package org.model;

import org.util.CSV;

public enum GeojsonProps {
    ID("id"),
    TITLE("title"),
    DATE("date"),
    LOCATION("location"),
    MAGNITUDE("magnitude"),
    DEEP("deep"),
    DISTANCE_TEXT("distance_text"),
    OBSERVATIONS("observations"),
    IN_CHARGE("in_charge"),
    REFERENCES("references");

    private final String colName;

    GeojsonProps(String colName) {
        this.colName = colName;
    }

    public static String getAllColsList(){
        return CSV.buildLineCSV(ID.toString(),
                TITLE.toString(),
                DATE.toString(),
                LOCATION.toString(),
                MAGNITUDE.toString(),
                DEEP.toString(),
                DISTANCE_TEXT.toString(),
                OBSERVATIONS.toString(),
                IN_CHARGE.toString(),
                REFERENCES.toString());
    }

    @Override
    public String toString() {
        return colName;
    }

}

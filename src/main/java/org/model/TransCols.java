package org.model;
import org.util.CSV;

public enum TransCols {
    ID("id"),
    TITLE("title"),
    MAGNITUDE("magnitude"),
    REGION("region"),
    DATE("date"),
    LOCATION("location"),
    DEEP("deep"),
    DISTANCE_TEXT("distance_text"),
    OBSERVATIONS("observations"),
    IN_CHARGE("in_charge"),
    REFERENCES("references");

    private final String colName;

    TransCols(String colName) {
        this.colName = colName;
    }

    public static String getAllColsList(){
        return CSV.buildLineCSV(ID.toString(),
                TITLE.toString(),
                MAGNITUDE.toString(),
                REGION.toString(),
                DATE.toString(),
                LOCATION.toString(),
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

package org.etl;

import org.model.Configuration;
import org.model.ExtractCols;
import org.model.TransCols;
import org.util.CSV;
import org.util.UtilNumber;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Transformer {

    public void transformData(Configuration conf){

        String stageExtractFilePath = conf.getStageExtractFilename();
        String stageTransformFilePath = conf.getStageTransformFilename();
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(stageExtractFilePath), StandardCharsets.UTF_16))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (index == 0) {
                    sb.append(TransCols.getAllColsList());
                } else {
                    String[] ls = line.split(",");
                    if(!areEmptyValues(ls)){
                        String dataRaw = CSV.buildLineCSV(
                            ls[ExtractCols.ID.ordinal()],
                            getTitle(ls[ExtractCols.TITLE.ordinal()]),
                            getMagnitude(ls[ExtractCols.MAGNITUDE.ordinal()]),
                            ls[ExtractCols.REGION.ordinal()].replace(";", "-"),
                            getDate(ls[ExtractCols.DATE.ordinal()]),
                            ls[ExtractCols.LOCATION.ordinal()].split(";")[0].trim(),
                            ls[ExtractCols.LOCATION.ordinal()].split(";")[1].trim(),
                            getDeep(ls[ExtractCols.DEEP.ordinal()]),
                            ls[ExtractCols.DISTANCE_TEXT.ordinal()],
                            ls[ExtractCols.OBSERVATIONS.ordinal()],
                            ls[ExtractCols.IN_CHARGE.ordinal()],
                            ls[ExtractCols.REFERENCES.ordinal()]
                        );
                        sb.append(dataRaw);
                    }
                }
                index ++;
            }
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(stageTransformFilePath), StandardCharsets.UTF_16)) {
                writer.write(sb.toString());
                System.out.println("File written successfully.");
            } catch (IOException e) {
                System.err.println("An error occurred while writing to the file: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    private boolean areEmptyValues(String[] values){
        return values[(ExtractCols.TITLE.ordinal())].trim().equals("M -") ||
                values[ExtractCols.MAGNITUDE.ordinal()].trim().equals("M") ||
                values[ExtractCols.REGION.ordinal()].trim().isEmpty() ||
                values[ExtractCols.DATE.ordinal()].trim().equals("(Hora local)") ||
                values[ExtractCols.LOCATION.ordinal()].trim().equals(";") ||
                values[ExtractCols.DEEP.ordinal()].trim().equals("km");
    }

    private String getTitle(String title) {
        title = title.split("-", 2)[1];
        title = title.replace(";", "-");
        return title;
    }

    private String getMagnitude(String magnitude){
        return UtilNumber.getDecimalNumber(magnitude);
    }

    private String getDate(String date) {
        date = date.replace("(Hora local)", "");
        date = date.trim();
        return date;
    }

    private String getDeep(String deep) {
        String formatedDeep = deep;
        if(deep.matches(".*\\d+.*")){
            formatedDeep = UtilNumber.getDecimalNumber(deep);
        }else{
            if(deep.toLowerCase().contains("supe")){
                formatedDeep = "35"; // shallow range (0 - 70 km), avg=35 km
            } else if (deep.toLowerCase().contains("intermedi")) {
                formatedDeep = "185"; //intermediate range (70 - 300 km), avg=115 km
            } else if (deep.toLowerCase().contains("profundo")) {
                formatedDeep = "500"; //intermediate range (300 - 700 km), avg=115 km
            }
        }
        return formatedDeep;
    }
}

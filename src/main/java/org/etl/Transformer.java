package org.etl;

import org.model.Configuration;
import org.model.Ecols;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transformer {

    public void transformData(Configuration conf){

        String stageExtractFilePath = conf.getStageExtractFilename();
        String stageTransformFilePath = conf.getStageTransformFilename();


        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(stageExtractFilePath), StandardCharsets.UTF_8))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                System.out.println("LINE: " + line);
                if (index == 0) {
                    content.append("id, title, magnitude, region, date, location, deep, distance_text, observations, in_charge, references\n");
                } else {
                    String[] ls = line.split(",");

                    //Check null or empty values
                    if(!areValuesEmpty(ls)){
                        String id = ls[Ecols.ID.ordinal()];
                        String title = ls[Ecols.TITLE.ordinal()].split("-", 2)[1];
                        String magnitude = getMagnitude(ls[Ecols.MAGNITUDE.ordinal()]);
                        String region = ls[Ecols.REGION.ordinal()].replace(";", "-");
                        String date = getDate(ls[Ecols.DATE.ordinal()]);
                        String location = ls[Ecols.LOCATION.ordinal()];
                        String deep = ls[Ecols.DEEP.ordinal()];
                        String distance_text = ls[Ecols.DISTANCE_TEXT.ordinal()];
                        String observations = ls[Ecols.OBSERVATIONS.ordinal()];
                        String in_charge = ls[Ecols.IN_CHARGE.ordinal()];
                        String references = ls[Ecols.REFERENCES.ordinal()];

                        String dataRaw = id + ", " +
                                title + ", " +
                                magnitude + ", " +
                                region + ", " +
                                date + ", " +
                                location + ", " +
                                deep + ", " +
                                distance_text + ", " +
                                observations + ", " +
                                in_charge + ", " +
                                references;


                        System.out.println(dataRaw);
                        content.append(dataRaw).append("\n");
                    }
                }
                index ++;
            }
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(stageTransformFilePath), StandardCharsets.UTF_16)) {
                writer.write(content.toString());
                System.out.println("File written successfully.");
            } catch (IOException e) {
                System.err.println("An error occurred while writing to the file: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private boolean areValuesEmpty(String[] ls){
        return ls[(Ecols.TITLE.ordinal())].trim().equals("M -") ||
                ls[Ecols.MAGNITUDE.ordinal()].trim().equals("M") ||
                ls[Ecols.REGION.ordinal()].trim().isEmpty() ||
                ls[Ecols.DATE.ordinal()].trim().equals("(Hora local)") ||
                ls[Ecols.LOCATION.ordinal()].trim().equals(";") ||
                ls[Ecols.DEEP.ordinal()].trim().equals("km");
//                ls[Ecols.DISTANCE_TEXT.ordinal()].trim().isEmpty() ||
//                ls[Ecols.OBSERVATIONS.ordinal()].trim().isEmpty() ||
//                ls[Ecols.IN_CHARGE.ordinal()].trim().isEmpty() ||
//                ls[Ecols.REFERENCES.ordinal()].trim().isEmpty();
    }

    private String getMagnitude(String str){
        String numberText = str.replace(";", ".");
        numberText = numberText.replaceAll("[a-zA-Z]", "");
        numberText = numberText.replaceAll("\\s", "");

        Pattern pattern = Pattern.compile("\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(numberText);

        if (matcher.find()) {
            numberText = matcher.group();
        }

        return numberText.trim();
    }

    private String getDate(String str) {
        String date = str.trim();
        date = date.replace("(Hora local)", "");
        date = date.trim();
        return date;
    }

}

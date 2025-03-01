package org.etl;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.model.Configuration;
import org.model.ExtractCols;
import org.util.CSV;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Extractor {

    public void extractData (Configuration conf){
        OkHttpClient client = new OkHttpClient();

        String url = conf.getSourceUrl();

        int lastID = conf.getIdLast();

        String fileName = conf.getStageExtractFilename();

        Path path = Paths.get(fileName);

        StringBuffer sb = new StringBuffer();

        sb.append(ExtractCols.getAllColsList());

        for (int id=1; id<=lastID; id++){

            Request request = new Request.Builder()
                    .url(url+id)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                int statusCode = response.code();

                // HTML content
                if (response.isSuccessful()) {
                    String htmlContent = response.body().string();
                    String dataRaw = getEarthquakeData(htmlContent, id);
                    System.out.print(dataRaw);
                    sb.append(dataRaw);
                } else {
                    System.out.println("GET request failed. Response Code: " + statusCode);
                }
            } catch (Exception e) {
                System.err.println("An error occurred while writing to the file: " + e.getMessage());
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_16)) {
            writer.write(sb.toString()); // Write text to the file
            System.out.println("File written successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private String getEarthquakeData(String html, int id) {

        Document document = Jsoup.parse(html);
        Element table = document.select("table").first();
        Elements rows = table.select("tr");

        int index = 0;
        String title = "";
        String magnitude = "";
        String region = "";
        String date = "";
        String location = "";
        String deep = "";
        String distance_text = "";
        String observations = "";
        String in_charge = "";
        String references = "";


        for (Element row : rows) {

            if (index > 9) {
                break;
            }
            Elements columns = row.select("td, th");
            switch (index) {
                case 1:
                    magnitude = columns.get(1).text().replace(',', ';');
                    break;
                case 2:
                    region = columns.get(1).text().replace(',', ';');
                    break;
                case 3:
                    date = columns.get(1).text().replace(',', ';');
                    break;
                case 4:
                    location = columns.get(1).text().replace(',', ';');
                    break;
                case 5:
                    deep = columns.get(1).text().replace(',', ';');
                    break;
                case 6:
                    distance_text = columns.get(1).text().replace(',', ';');
                    break;
                case 7:
                    observations = columns.get(1).text().replace(',', ';');
                    break;
                case 8:
                    in_charge = columns.get(1).text().replace(',', ';');
                    break;
                case 9:
                    references = columns.get(1).text().replace(',', ';');
                    break;
                default: // index = 0
                    title = columns.getFirst().text().replace(',', ';');
            }

            index++;
        }

        return CSV.buildLineCSV(String.valueOf(id), title, magnitude, region, date, location, deep, distance_text, observations, in_charge, references);

    }

}

package org.etl;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.model.Configuration;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.util.Properties;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main (String[] args) {
        Main main =  new Main();
        main.ETL();
    }

    public void ETL (){

        Configuration conf = loadProperties();

        if(conf.isStageExtract()){
            extractData(conf);
        }

        if(conf.isStageTransform()){
            transformData(conf);
        }

        if(conf.isStageLoad()){
            loadData(conf);
        }

    }

    private void extractData (Configuration conf){
        OkHttpClient client = new OkHttpClient();

        String url = conf.getSourceUrl();

        int lastID = conf.getIdLast();

        String fileName = conf.getStageExtractFilename();

        Path path = Paths.get(fileName);

        StringBuffer sb = new StringBuffer();

        sb.append("id, title, magnitude, region, date, location, deep, distance_text, observations, in_charge, references\n");

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
                    System.out.println(dataRaw);
                    sb.append(dataRaw).append("\n");
                } else {
                    System.out.println("GET request failed. Response Code: " + statusCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_16)) {
            writer.write(sb.toString()); // Write text to the file
            System.out.println("File written successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private void transformData(Configuration conf){
        // covert info in correct data

    }

    private void loadData(Configuration conf){

    }

    private String getEarthquakeData(String html, int id){

        Document document = Jsoup.parse(html);
        Element table = document.select("table").first();
        Elements rows = table.select("tr");

        int index=0;
        String title="";
        String magnitude="";
        String region="";
        String date="";
        String location="";
        String deep="";
        String distance_text="";
        String observations="";
        String in_charge="";
        String references="";


        for (Element row : rows) {

            if(index>9){
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
                    title = columns.get(0).text().replace(',', ';');
            }

            index++;
        }

        return id + ", " +
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
    }

    private Configuration loadProperties(){
        Properties properties = new Properties();
        Configuration conf = new Configuration();

        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);

            conf.setSourceUrl(properties.getProperty("source.url"));
            conf.setIdLast(Integer.parseInt(properties.getProperty("id.last")));

            conf.setStageExtract(Boolean.parseBoolean(properties.getProperty("stage.extract")));
            conf.setStageExtractFilename(properties.getProperty("stage.extract.filename"));

            conf.setStageTransform(Boolean.parseBoolean(properties.getProperty("stage.transform")));
            conf.setStageTransformFilename(properties.getProperty("stage.transform.filename"));

            conf.setStageLoad(Boolean.parseBoolean(properties.getProperty("stage.load")));
            conf.setStageLoadFilename(properties.getProperty("stage.load.filename"));


        } catch (IOException e) {
            System.err.println("Error reading the configuration file: " + e.getMessage());
        }
        return conf;
    }
}
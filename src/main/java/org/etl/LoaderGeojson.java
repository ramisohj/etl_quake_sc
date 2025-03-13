package org.etl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.locationtech.jts.io.ParseException;
import org.model.Configuration;
import org.model.GeojsonPoint;
import org.model.GeojsonPointList;
import org.model.TransCols;
import org.util.NumberUtil;
import org.util.StringUtil;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;


public class LoaderGeojson {

    public void loadGeojson(Configuration conf){

        String stageTransformFilePath = conf.getStageTransformFilename();
        String stageLoadGeojsonFilenameFilePath = conf.getStageLoadGeoFilename();
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(stageTransformFilePath), StandardCharsets.UTF_16))) {
            String line;

            // reading headers
            br.readLine();

            GeojsonPointList geoPointList = new GeojsonPointList();

            while((line = br.readLine()) != null){
                String[] ls = line.split(",");

                GeojsonPoint geoPoint = new GeojsonPoint();
                geoPoint.addProperty("id", NumberUtil.getIntegerNumber(ls[TransCols.ID.ordinal()]));
                geoPoint.addProperty("title", StringUtil.trim(ls[TransCols.TITLE.ordinal()]));
                geoPoint.addProperty("date", StringUtil.trim(ls[TransCols.DATE.ordinal()]));
                geoPoint.addProperty("mag", NumberUtil.getDecimalNumber(ls[TransCols.MAGNITUDE.ordinal()]));
                geoPoint.addProperty("deep", NumberUtil.getDecimalNumber(ls[TransCols.DEEP.ordinal()]));
                geoPoint.addProperty("distance_text", StringUtil.trim(ls[TransCols.DISTANCE_TEXT.ordinal()]));
                geoPoint.addProperty("observations", StringUtil.trim(ls[TransCols.OBSERVATIONS.ordinal()]));
                geoPoint.addProperty("in_charge", StringUtil.trim(ls[TransCols.IN_CHARGE.ordinal()]));
                geoPoint.addProperty("references", StringUtil.trim(ls[TransCols.REFERENCES.ordinal()]));
                geoPoint.addGeometry(ls[TransCols.LOCATION.ordinal()]);

                geoPointList.addPoint(geoPoint);
            }

            // Write to GeoJSON file
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            try {
                mapper.writeValue(new File(String.valueOf(Paths.get(stageLoadGeojsonFilenameFilePath))), geoPointList.getGeojsonPointList());
                System.out.println("✅ GeoJSON FeatureCollection file created: feature_collection.geojson");
            } catch (IOException e) {
                System.err.println("An error occurred while writing to the file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

package org.etl;

import org.model.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    public static Configuration loadProperties(){
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

            conf.setStageLoadGeo(Boolean.parseBoolean(properties.getProperty("stage.load.geo")));
            conf.setStageLoadGeoFilename(properties.getProperty("stage.load.geo.filename"));

        } catch (IOException e) {
            System.err.println("Error reading the configuration file: " + e.getMessage());
        }
        return conf;
    }
}

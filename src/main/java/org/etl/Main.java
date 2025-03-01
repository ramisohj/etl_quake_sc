package org.etl;

import org.model.Configuration;

public class Main {

    public static void main (String[] args) {
        Main main =  new Main();
        main.ETL();
    }

    public void ETL (){

        Configuration conf = Config.loadProperties();

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

    private void extractData(Configuration conf){
        Extractor extractor = new Extractor();
        extractor.extractData(conf);
    }

    private void transformData(Configuration conf){
        Transformer transformer = new Transformer();
        transformer.transformData(conf);
    }

    private void loadData(Configuration conf){

    }
}
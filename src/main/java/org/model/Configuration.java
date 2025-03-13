package org.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Configuration {

    private String sourceUrl;
    private int idLast;

    private boolean stageExtract;
    private String stageExtractFilename;

    private boolean stageTransform;
    private String stageTransformFilename;

    private boolean stageLoad;
    private String stageLoadFilename;

    private boolean stageLoadGeo;
    private String stageLoadGeoFilename;
}

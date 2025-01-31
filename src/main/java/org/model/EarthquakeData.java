package org.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EarthquakeData {

    private int id;
    private String title;
    private int magnitude;
    private String region;
    private Date date;
    private float longitude;
    private float latitude;
    private float deep;
    private String distance_text;
    private String observations;
    private String inCharge;
    private String references;
}

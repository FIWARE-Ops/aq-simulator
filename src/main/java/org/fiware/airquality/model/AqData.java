package org.fiware.airquality.model;

import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * Data object to represent the AirQualityObserved entity.
 */
@Data
public class AqData {

	private final String id;
	private final String type = "AirQualityObserved";

	private Attribute<Double> co;
	private Attribute<Double> h2s;
	private Attribute<Double> no;
	private Attribute<Double> no2;
	private Attribute<Double> o3;
	private Attribute<Double> pm1;
	private Attribute<Double> pm10;
	private Attribute<Double> pm25;
	private Attribute<Double> so2;
	private Attribute<String> timeInstant;
	private Attribute<Double> voc;
	private Attribute<String> dataProvider;
	private Attribute<Double> humidity;
	private Attribute<Boolean> ica;
	private Attribute<Point> location;
	private Attribute<String> name;
	private Attribute<String> operationalStatus;
	private Attribute<List<String>> particulates;
	private Attribute<List<String>> pollutants;
	private Attribute<String> source;
	private Attribute<Double> temperature;
	private Attribute<String> dateModified;


}

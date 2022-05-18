package org.fiware.airquality.config;

import lombok.Data;

@Data
public class AqSimulation {

	private String id;
	private double lat = 38.07;
	private double longi = -1.271;
	private int sampleInterval = 30;

	// historic data to be generated, in days
	private int age = 10;

}

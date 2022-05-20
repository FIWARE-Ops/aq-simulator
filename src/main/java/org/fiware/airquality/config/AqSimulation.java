package org.fiware.airquality.config;

import lombok.Data;

/**
 * Config object for a single aq-simulator.
 */
@Data
public class AqSimulation {

	// Id to be used for the simulator
	private String id;
	// name of the device to be simulated
	private String name = "test-device";
	// latitude to be used
	private double lat = 38.07;
	// longitude to be used
	private double longi = -1.271;
	// interval to sample the data, in seconds
	private int sampleInterval = 30;

	// historic data to be generated, in days
	private int age = 10;

}

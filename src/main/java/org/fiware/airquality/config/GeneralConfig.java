package org.fiware.airquality.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration of the simulator
 */
@ConfigurationProperties("general")
@Data
public class GeneralConfig {

	// delay data generation for a certain amount of seconds to avoid race-conditions with downstream subscribers
	private int startupDelay = 120;

	// minutes between historic datapoints
	private int historicDensity = 15;
	// fiware service to be used
	private String fiwareService = "";
	// fiware service path to be used
	private String fiwareServicePath= "/";
	// url of the broker
	private String brokerUrl = "http://localhost:1026";
	// list of simulations to run
	private List<AqSimulation> aqSimulations = new ArrayList<>();

}

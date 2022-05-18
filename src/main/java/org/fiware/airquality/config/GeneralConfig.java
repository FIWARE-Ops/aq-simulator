package org.fiware.airquality.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("general")
@Data
public class GeneralConfig {

	// minutes between historic datapoints
	private int historicDensity = 15;
	private String fiwareService = "";
	private String fiwareServicePath= "/";
	private String brokerUrl = "http://localhost:1026";
	private List<AqSimulation> aqSimulations = new ArrayList<>();

}

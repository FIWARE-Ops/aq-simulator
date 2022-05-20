package org.fiware.airquality;

import io.micronaut.context.annotation.Context;
import io.micronaut.http.client.netty.DefaultHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.airquality.config.AqSimulation;
import org.fiware.airquality.config.GeneralConfig;
import org.fiware.airquality.model.AirQualityObserved;
import org.fiware.airquality.model.AqData;
import org.fiware.airquality.model.Attribute;
import org.fiware.airquality.model.Point;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Simulator for reading out the config and starting the actual simulator instances
 */
@Slf4j
@Context
@RequiredArgsConstructor
public class Simulator {

	private final GeneralConfig generalConfig;
	private final Clock clock;
	private final ScheduledExecutorService scheduledExecutorService;
	private final DefaultHttpClient defaultHttpClient;

	@PostConstruct
	public void simulate() {
		log.info("Start to simulate data for {} ag-sensors.", generalConfig.getAqSimulations().size());

		generalConfig.getAqSimulations().forEach(aqSimulation -> {
			createAirQualityObserved(aqSimulation).startSimulation(aqSimulation.getAge(), generalConfig.getHistoricDensity(), aqSimulation.getSampleInterval(), generalConfig.getStartupDelay());
		});
	}

	private AirQualityObserved createAirQualityObserved(AqSimulation aqSimulation) {
		String id = Optional.ofNullable(aqSimulation.getId()).orElseGet(() -> String.format("urn:ngsi:AirQualityObserved:%s", UUID.randomUUID()));
		Attribute<Point> locationAttribute = new Attribute<>("geo:json", new Point(List.of(aqSimulation.getLongi(), aqSimulation.getLat())));
		Attribute<String> nameAttribute = new Attribute<>("Text", aqSimulation.getName());
		AqData aqData = new AqData(id);
		aqData.setLocation(locationAttribute);
		aqData.setName(nameAttribute);
		return new AirQualityObserved(clock, defaultHttpClient, scheduledExecutorService, generalConfig.getBrokerUrl(), generalConfig.getFiwareService(), generalConfig.getFiwareServicePath(), aqData);

	}
}

package org.fiware.airquality;

import com.sun.source.doctree.SeeTree;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requirements;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.netty.DefaultHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.airquality.config.AqSimulation;
import org.fiware.airquality.config.GeneralConfig;
import org.fiware.airquality.model.AirQualityObserved;
import org.fiware.airquality.model.AqData;
import org.fiware.airquality.model.Attribute;

import javax.annotation.PostConstruct;
import java.net.HttpCookie;
import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

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
			createAirQualityObserved(aqSimulation).startSimulation(aqSimulation.getAge(), generalConfig.getHistoricDensity(), aqSimulation.getSampleInterval());
		});
	}

	private AirQualityObserved createAirQualityObserved(AqSimulation aqSimulation) {
		String id = Optional.ofNullable(aqSimulation.getId()).orElseGet(() -> String.format("urn:ngsi:AirQualityObserved:%s", UUID.randomUUID()));
		Attribute<List<Double>> locationAttribute = new Attribute<>();
		locationAttribute.setValue(List.of(aqSimulation.getLat(), aqSimulation.getLongi()));
		AqData aqData = new AqData(id);
		aqData.setLocation(locationAttribute);
		return new AirQualityObserved(clock, defaultHttpClient, scheduledExecutorService, generalConfig.getBrokerUrl(), generalConfig.getFiwareService(), generalConfig.getFiwareServicePath(), aqData);

	}
}

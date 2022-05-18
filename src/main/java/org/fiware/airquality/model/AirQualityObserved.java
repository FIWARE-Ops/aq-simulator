package org.fiware.airquality.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.reactivex.Flowable;
import io.reactivex.Single;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;

import javax.management.AttributeList;
import java.net.URI;
import java.net.URL;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
@RequiredArgsConstructor
public class AirQualityObserved {
	private static final DateTimeFormatter DATE_TIME_FORMATTER;

	static {
		String patternFormat = "yyyy-MM-dd'T'hh:mm:ssZ";
		DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(patternFormat)
				.withZone(ZoneId.systemDefault());
	}

	private final Clock clock;
	private final HttpClient httpClient;
	private final ScheduledExecutorService scheduledExecutorService;
	private final String url;
	private final String fiwareService;
	private final String fiwareServicePath;


	private final AqData aqData;

	public void startSimulation(int age, int historicDensity, int sampleInterval) {
		log.info("Start simulation for {}", aqData.getId());
		generateHistoricData(age, historicDensity);
		scheduledExecutorService.scheduleAtFixedRate(this::updateData, getRandomNumber(0, 30).intValue(), sampleInterval, TimeUnit.SECONDS);
	}

	private void updateData() {
		updateData(clock.instant());
		sendUpdate();
	}

	private void generateHistoricData(int age, int historicDensity) {
		Instant currentTime = clock.instant();
		Instant maxAge = currentTime.minus(Duration.of(age, ChronoUnit.DAYS));
		while (maxAge.isAfter(currentTime)) {
			updateData(currentTime);
			sendUpdate();
			currentTime = currentTime.minus(Duration.of(historicDensity, ChronoUnit.MINUTES));
		}

	}

	private void sendUpdate() {
		log.info("Update {}", aqData.getId());
		HttpRequest<AqData> httpRequest = HttpRequest
				.create(HttpMethod.POST, String.format("%s/v2/entities?options=upsert", url))
				.headers(Map.of("Fiware-Service", fiwareService, "Fiware-ServicePath", fiwareServicePath))
				.body(aqData);
		Single.fromPublisher(httpClient.exchange(httpRequest)).doOnError((e) -> log.warn("Update failed.", e)).subscribe();
	}

	private void updateData(Instant dateObserved) {
		MetaDataEntry<String> dateObservedMetaData = new MetaDataEntry<>("DateTime", DATE_TIME_FORMATTER.format(dateObserved));

		aqData.setCo(generateDoubleAttribute(dateObservedMetaData, 0, 60000, "0,9999,good|10000,29999,unhealthyForSensitiveGroups|30000,49999,unhealthy|50000,-,hazardous", "CO value"));
		aqData.setH2s(generateDoubleAttribute(dateObservedMetaData, 0, 30000, "0,4999,good|5000,9999,unhealthyForSensitiveGroups|10000,19999,unhealthy|20000,-,hazardous", "H2S value"));
		aqData.setNo(generateDoubleAttribute(dateObservedMetaData, 0, 30000, "0,2499,good|2500,6999,unhealthyForSensitiveGroups|7000,19999,unhealthy|20000,-,hazardous", "NO value"));
		aqData.setNo2(generateDoubleAttribute(dateObservedMetaData, 0, 1100, "0,199,good|200,559,unhealthyForSensitiveGroups|560,999,unhealthy|1000,-,hazardous", "NO2 value"));
		aqData.setO3(generateDoubleAttribute(dateObservedMetaData, 0, 350, "0,119,good|120,239,unhealthyForSensitiveGroups|240,299,unhealthy|300,-,hazardous", "03 value"));
		aqData.setPm1(generateDoubleAttribute(dateObservedMetaData, 0, 110, "0,14,good|15,59,unhealthyForSensitiveGroups|60,99,unhealthy|100,-,hazardous", "PM1 value"));
		aqData.setPm10(generateDoubleAttribute(dateObservedMetaData, 0, 210, "0,49,good|50,149,unhealthyForSensitiveGroups|150,199,unhealthy|200,-,hazardous", "PM10 value"));
		aqData.setPm25(generateDoubleAttribute(dateObservedMetaData, 0, 210, "0,49,good|50,149,unhealthyForSensitiveGroups|150,199,unhealthy|200,-,hazardous", "PM2.5 value"));
		aqData.setSo2(generateDoubleAttribute(dateObservedMetaData, 0, 2100, "0,349,good|350,999,unhealthyForSensitiveGroups|1000,1999,unhealthy|2000,-,hazardous", "SO2 value"));
		aqData.setVoc(generateDoubleAttribute(dateObservedMetaData, 900, 1300, "", "VOC value"));

		aqData.setTimeInstant(new Attribute<>("DateTime", DATE_TIME_FORMATTER.format(dateObserved)));
		aqData.setDataProvider(new Attribute<>("Text", "www.hopu.eu"));
		aqData.setHumidity(new Attribute<>("Number", getRandomNumber(0, 100)));
		aqData.setIca(new Attribute<>("Boolean", getRandomNumber(0, 1) > 0.5));
		aqData.setName(new Attribute<>("Text", "Calle Mayor / Plaza Adolfo Suárez"));
		aqData.setOperationalStatus(new Attribute<>("AirQualityObserved", getRandomNumber(0, 1) < 0.1 ? "DISCONNECTED" : "CONNECTED"));
		aqData.getOperationalStatus().setMetadata(Map.of("dateObserved", dateObservedMetaData));
		aqData.setParticulates(new Attribute<>("List", List.of("PM1", "PM10", "PM25")));
		MetaDataEntry<Boolean> showMD = new MetaDataEntry<>("Boolean", true);
		aqData.getParticulates().setMetadata(Map.of("show", showMD));
		aqData.setPollutants(new Attribute<>("List", List.of("CO", "H2S", "NO", "NO2", "O3", "SO2", "VOC")));
		aqData.getPollutants().setMetadata(Map.of("show", showMD));
		aqData.setSource(new Attribute<>("Text", "www.hopu.eu"));
		aqData.setTemperature(new Attribute<>("Number", getRandomNumber(0, 45)));
		aqData.setDateModified(new Attribute<>("DateTime", DATE_TIME_FORMATTER.format(dateObserved)));
	}

	private Attribute<Double> generateDoubleAttribute(MetaDataEntry<String> dateObservedMetaData, int min, int max, String range, String desc) {
		Attribute<Double> doubleAttribute = new Attribute<>();
		doubleAttribute.setValue(getRandomNumber(min, max));
		MetaDataEntry<String> metaDataDescEntry = new MetaDataEntry<>("Text", desc);
		MetaDataEntry<String> metaDataRangeEntry = new MetaDataEntry<>("Text", range);
		MetaDataEntry<String> metaDataUnitCodeEntry = new MetaDataEntry<>("Text", "GQ");
		doubleAttribute.setMetadata(Map.of("dateObserved", dateObservedMetaData,
				"description", metaDataDescEntry,
				"ranges", metaDataRangeEntry,
				"unitCode", metaDataUnitCodeEntry));
		return doubleAttribute;
	}


	public Double getRandomNumber(int min, int max) {
		return ((Math.random() * (max - min)) + min);
	}
}

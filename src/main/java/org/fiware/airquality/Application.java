package org.fiware.airquality;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.runtime.Micronaut;
import org.fiware.airquality.config.KeycloakConfig;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import java.time.Clock;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Entry point for the application
 */
@Factory
public class Application {

	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
	}

	@Bean
	public ScheduledExecutorService simulationExecutor() {
		return Executors.newScheduledThreadPool(1);
	}

	@Bean
	public Clock clock() {
		return Clock.systemUTC();
	}

	@Bean
	@Requires(property = "keycloak.enabled", value = "true")
	public Keycloak keycloak(KeycloakConfig keycloakConfig) {
		return KeycloakBuilder.builder()
				.username(keycloakConfig.getUsername())
				.password(keycloakConfig.getPassword())
				.clientSecret(keycloakConfig.getClientSecret())
				.clientId(keycloakConfig.getClientId())
				.grantType("password")
				.realm(keycloakConfig.getRealm())
				.serverUrl(keycloakConfig.getUrl())
				.build();
	}

}

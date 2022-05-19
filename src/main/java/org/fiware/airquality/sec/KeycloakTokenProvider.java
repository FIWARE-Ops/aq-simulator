package org.fiware.airquality.sec;

import lombok.RequiredArgsConstructor;
import org.fiware.airquality.config.KeycloakConfig;
import org.keycloak.admin.client.KeycloakBuilder;

import javax.inject.Singleton;

@RequiredArgsConstructor
@Singleton
public class KeycloakTokenProvider {

	private final KeycloakConfig keycloakConfig;

	private void t() {
	}
}

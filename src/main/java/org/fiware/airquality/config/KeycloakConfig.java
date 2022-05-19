package org.fiware.airquality.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;

@ConfigurationProperties("keycloak")
@Data
public class KeycloakConfig {

	private boolean enabled;
	private String clientId;
	private String clientSecret;
	private String username;
	private String password;
	private String realm;
	private String url;
}

package org.fiware.airquality.sec;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import io.reactivex.Flowable;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.reactivestreams.Publisher;

import java.util.Optional;

@RequiredArgsConstructor
@Filter
@Requires(property = "keycloak.enabled", value = "true")
public class AuthenticationHttpFilter implements HttpClientFilter {

	private final Keycloak keycloak;

	@Override
	public Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
		AccessTokenResponse accessTokenResponse = keycloak.tokenManager().getAccessToken();
		Optional.ofNullable(accessTokenResponse.getError()).ifPresent(error -> {
			throw new AuthenticationException(String.format("Was not able to get an access token. Error: %s", error));
		});
		if (accessTokenResponse.getToken() != null) {
			request = request.bearerAuth(accessTokenResponse.getToken());
		}
		return chain.proceed(request);
	}
}

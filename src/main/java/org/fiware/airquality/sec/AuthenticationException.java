package org.fiware.airquality.sec;

/**
 * Exception to be thrown in case of auth error.
 */
public class AuthenticationException extends RuntimeException {

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}

package org.fiware.airquality.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Dataobject representing an attribute
 */
@Data
@NoArgsConstructor
public class Attribute<T> {

	private String type;
	private T value;
	private Map<String, MetaDataEntry> metadata;

	public Attribute(String type, T value) {
		this.type = type;
		this.value = value;
	}
}

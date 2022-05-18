package org.fiware.airquality.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Dataobject representing a single metadata entry.
 */
@RequiredArgsConstructor
@Data
public class MetaDataEntry<T> {

	private final String type;
	private final T value;
}

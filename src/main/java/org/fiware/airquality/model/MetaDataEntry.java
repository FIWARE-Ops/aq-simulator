package org.fiware.airquality.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class MetaDataEntry<T> {

	private final String type;
	private final T value;
}

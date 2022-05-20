package org.fiware.airquality.model;

import io.micronaut.core.convert.format.ReadableBytes;
import lombok.Data;

import java.util.List;

@Data
@ReadableBytes
public class Point {

	private final String type = "Point";
	private final List<Double> coordinates;
}

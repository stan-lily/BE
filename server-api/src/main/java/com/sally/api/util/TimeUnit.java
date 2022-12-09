package com.sally.api.util;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.temporal.ChronoUnit;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum TimeUnit {
	DAY, WEEK, MONTH;

	private static Map<TimeUnit, ChronoUnit> mapper = Map.of(
		DAY, ChronoUnit.DAYS,
		WEEK, ChronoUnit.WEEKS,
		MONTH, ChronoUnit.MONTHS);

	public ChronoUnit toUnit() {
		return mapper.get(this);
	}

	@JsonCreator
	public static TimeUnit from(String timeUnit) {
		return TimeUnit.valueOf(timeUnit.toUpperCase());
	}
}

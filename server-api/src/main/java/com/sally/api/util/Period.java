package com.sally.api.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

import lombok.Data;

@Data
public class Period {
	private final LocalDate startDate;
	private final LocalDate endDate;

	public static Period of(LocalDate startDate, LocalDate endDate) {
		return new Period(startDate, endDate);
	}

	/**
	 * 올해 첫 번째 날, 마지막 날
	 * @return
	 */
	public static Period dayOfYear() {
		LocalDate firstDayThisYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
		LocalDate lastDayThisYear = LocalDate.now().with(TemporalAdjusters.lastDayOfYear());
		return new Period(firstDayThisYear, lastDayThisYear);
	}

	public LocalDate startAtWeeksAgo(long weekUnit) {
		return startDate.minusWeeks(weekUnit);
	}

	/**
	 * 등록 기간은 제한 기간을 넘어가지 않아야 한다.
	 * @param limitWeekSize
	 * @return
	 */
	public boolean isWithin(long limitWeekSize) {
		long weeks = ChronoUnit.WEEKS.between(startDate, endDate);
		return weeks < limitWeekSize;
	}

	public LocalDate startDate() {
		return startDate;
	}

	public LocalDate endDate() {
		return endDate;
	}
}

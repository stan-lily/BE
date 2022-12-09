package com.sally.api.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

class DateHelper {
	private final LocalDate startAt;
	private final LocalDate endAt;

	DateHelper(LocalDate startAt, LocalDate endAt) {
		this.startAt = startAt;
		this.endAt = endAt;
	}

	LocalDate startDate() {
		return this.startAt;
	}

	LocalDate endDate() {
		return this.endAt;
	}

	boolean isSameAsYear() {
		return (this.startAt.getYear() == endAt.getYear());
	}

	boolean isDifferentOfDate() {
		return this.startAt.compareTo(endAt) != 0;
	}

	boolean isSameAsMonth() {
		return startAt.getMonth().compareTo(endAt.getMonth()) == 0;
	}

	private static LocalDate toLocalDate(LocalDateTime dateTime) {
		return dateTime.toLocalDate();
	}
}

public class PeriodFormatter {
	private static final String WAVY_MARK = " ~ ";
	static final String YEAR_AND_DATE_PATTERN = "yyyy년 MM월 dd일";
	static final String DATE_PATTERN = "dd일";
	private static final DateTimeFormatter YEAR_AND_DATE_FORMATTER = DateTimeFormatter.ofPattern(YEAR_AND_DATE_PATTERN);
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

	/**
	 * 기간 정보를 압축하여 출력한다.
	 */
	public static String toDate(Period period) {
		DateHelper date = new DateHelper(period.startDate(), period.endDate());
		if (date.isSameAsYear() && date.isSameAsMonth()) {
			if (date.isDifferentOfDate()) {
				return toStr(WAVY_MARK, YEAR_AND_DATE_FORMATTER.format(date.startDate()),
					DATE_FORMATTER.format(date.endDate()));
			}
			return YEAR_AND_DATE_FORMATTER.format(date.startDate());
		}
		return toStr(WAVY_MARK, YEAR_AND_DATE_FORMATTER.format(date.startDate()),
			YEAR_AND_DATE_FORMATTER.format(date.endDate()));
	}

	private static String toStr(String delimiter, Object... dateOrTimes) {
		return Arrays.stream(dateOrTimes)
			.map(String::valueOf)
			.collect(Collectors.joining(delimiter));
	}
}

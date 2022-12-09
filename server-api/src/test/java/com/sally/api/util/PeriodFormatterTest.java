package com.sally.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

class PeriodFormatterTest {
	public static final int START_YEAR = 2022;
	public static final int START_MONTH = 8;
	public static final int START_DAY = 26;
	public static final LocalDate START_AT = LocalDate.of(START_YEAR, START_MONTH, START_DAY);

	private static final String DATE_PATTERN = "yyyy년 MM월 dd일";
	private final String PERIOD_DATE_PATTERN = "%s ~ %s";

	@ParameterizedTest
	@MethodSource("providerSameDate")
	@DisplayName("시작일과 종료일의 날짜가 같으면 제목의 기간 정보는 시작 날짜만 출력한다.")
	void toDate_subjectWithDateInformation_sameDate(LocalDate startAt, LocalDate endAt) {
		Period period = Period.of(startAt, endAt);
		String expected = toDateFormatStr(period.startDate());

		String actual = PeriodFormatter.toDate(period);

		assertThat(actual).isEqualTo(expected);
	}

	@ParameterizedTest
	@MethodSource("providerDifferentDay")
	@DisplayName("시작일과 종료일의 일만 다르다면 제목의 기간 정보는 [시작 날짜 ~ 종료일] 형태로 출력한다.")
	void toDate_subjectWithDateInformation_sameYearMonthAndDifferentDay(LocalDate startAt, LocalDate endAt) {
		Period period = Period.of(startAt, endAt);
		String expected = toDateFormatStr(period.startDate()) + String.format(" ~ %d일", endAt.getDayOfMonth());

		String actual = PeriodFormatter.toDate(period);

		assertThat(actual).isEqualTo(expected);
	}

	@ParameterizedTest
	@MethodSource("providerAnotherDate")
	@DisplayName("시작일과 종료일의 년도나 월이 다르면 제목의 기간 정보는 [시작 날짜 ~ 종료날짜] 형태로 출력한다.")
	void toDate_subjectWithDateInformation_anotherDate(LocalDate startAt, LocalDate endAt) {
		Period period = Period.of(startAt, endAt);
		String expected = String.format(
			PERIOD_DATE_PATTERN,
			toDateFormatStr(period.startDate()),
			toDateFormatStr(period.endDate()));

		String actual = PeriodFormatter.toDate(period);

		assertThat(actual).isEqualTo(expected);
	}

	private String toDateFormatStr(LocalDate dateTime) {
		return DateTimeFormatter.ofPattern(DATE_PATTERN).format(dateTime);
	}

	private static Stream<Arguments> providerDifferentDay() { // argument source method
		return Stream.of(
			Arguments.of(START_AT, START_AT.plusDays(1)),
			Arguments.of(START_AT.plusDays(10), START_AT.plusDays(20)),
			Arguments.of(START_AT.plusMonths(1), START_AT.plusMonths(1).plusDays(2))

		);
	}

	private static Stream<Arguments> providerSameDate() { // argument source method
		return Stream.of(
			Arguments.of(START_AT, START_AT),
			Arguments.of(START_AT.plusDays(10), START_AT.plusDays(10)),
			Arguments.of(START_AT.plusMonths(1), START_AT.plusMonths(1))
		);
	}

	private static Stream<Arguments> providerAnotherDate() { // argument source method
		return Stream.of(
			Arguments.of(START_AT, START_AT.plusYears(1)),
			Arguments.of(START_AT, START_AT.plusMonths(1)),
			Arguments.of(START_AT, START_AT.plusDays(11))
			// Arguments.of(START_AT, START_AT.plusDays(2))
		);
	}
}

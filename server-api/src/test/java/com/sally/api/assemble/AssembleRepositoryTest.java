package com.sally.api.assemble;

import static com.sally.api.TestObjectMapper.ASSEMBLE_TEST_FOR_TITLE;
import static com.sally.api.assemble.Assemble.ASSEMBLE_LIMITS_OF_NUMBER_FOR_WEEKS;
import static org.assertj.core.api.Assertions.assertThat;

import com.sally.api.TestObjectMapper;
import com.sally.api.project.Project;
import com.sally.api.project.ProjectRepository;
import com.sally.api.util.Period;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class PeriodAggregator implements ArgumentsAggregator {
	@Override
	public Period aggregateArguments(ArgumentsAccessor arguments, ParameterContext context) {
		return Period.of(
			arguments.get(0, LocalDate.class),
			arguments.get(1, LocalDate.class));
	}
}

@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class AssembleRepositoryTest {
	private final AssembleRepository assembleRepository;
	private final ProjectRepository projectRepository;

	private Project project;

	public AssembleRepositoryTest(
		@Autowired AssembleRepository assembleRepository,
		@Autowired ProjectRepository projectRepository) {
		this.assembleRepository = assembleRepository;
		this.projectRepository = projectRepository;
	}

	/**
	 * 저장된 기간과 중복 파라미터에 대한 optional assemble 결과 확인
	 * 중복 안 될 경우 null 확인
	 *
	 * 저장된 날짜 : 220701 ~ 220710, 220720 ~ 220725
	 * - 등록 가능 한 날짜 : 220711 ~ 220719, 220810 ~ 220820
	 * - 예외 발생 확인 날짜
	 * 	- 220629 ~ 220701, 220629 ~ 220705, 2206029 ~ 2207010 ( 마감일이 이후의 시작일 이후기간과 겹침)
	 * 	- 220705 ~ 220711, 220710 ~ 220711, 220710 ~ 220720  ( 앞의 날짜가 이전 등록된 기간과 겹침, 마감일이 이후 시작일과 겹침)
	 * 	- 220715 ~ 220723 ( 등록된 2개의 기간과 겹침)
	 * 	- 220816 ~ 220820 (마지막 마감날짜 이후 3주 초과)
	 *
	 * 	- 시작일 ~ 마감일 기간 3주 이내
	 */
	private List<Assemble> assembles;

	@BeforeEach
	void beforeEach() {
		project = projectRepository.saveAndFlush(TestObjectMapper.project());
		assembles = TestObjectMapper.assemble(project);
		assembleRepository.saveAllAndFlush(assembles);
	}

	@DisplayName("Assemble 목록 조회는 올해 등록된 정보만 가져온다.")
	@Test
	void ok() {
		Period dayOfYear = Period.dayOfYear();
		Assemble expected = assembles.stream()
			.max((a, b) -> a.startAt().compareTo(b.startAt()))
			.get();  // 2022년도의 다음 해

		List<Assemble> actuals = assembleRepository.findAllByProjectIdAndStartAtIsBetween(
			project.id(), dayOfYear.getStartDate(), dayOfYear.getEndDate());

		assertThat(dayOfYear.getEndDate().getYear()).isNotEqualTo(expected.startAt().getYear());
		assertThat(actuals).extracting("startAt").doesNotContain(expected.startAt().getYear());
	}

	@Nested
	@DisplayName("Assemble 제목은")
	class A_create_Assemble_by_title {
		@DisplayName("중복시 조회 결과 true 를 반환한다.")
		@ParameterizedTest(name = "[{index}] : {0}")
		@ValueSource(strings = ASSEMBLE_TEST_FOR_TITLE)
		void ok(String title) {
			boolean actual = assembleRepository.existsByProjectIdAndTitleContains(project.id(), title);

			assertThat(actual).isTrue();
		}

		@DisplayName("unique 하면 false 를 반환한다.")
		@ParameterizedTest(name = "[{index}] : {0}")
		@ValueSource(strings = {"없는 제목", "unique"})
		void fail(String title) {
			boolean actual = assembleRepository.existsByProjectIdAndTitleContains(project.id(), title);
			assertThat(actual).isFalse();
		}
	}

	@Nested
	@DisplayName("등록된 Assemble 마지막 일로 부터 제한기간(" + ASSEMBLE_LIMITS_OF_NUMBER_FOR_WEEKS + ") 주 이내에")
	class A_create_Assemble_by_period_within_limits {
		@DisplayName("등록된 종료일이 있으면 true 를 반환한다.")
		@ParameterizedTest(name = "[{index}] : {0} ~ {1}")
		@CsvSource({
			"2022-07-11, 2022-07-19",
			"2022-08-10, 2022-08-20",
			"2022-08-15, 2022-08-20"
		})
		void ok(@AggregateWith(PeriodAggregator.class) Period period) {
			boolean actual = assembleRepository.existsByProjectIdAndEndAtBetween(project.id(),
				period.startAtWeeksAgo(ASSEMBLE_LIMITS_OF_NUMBER_FOR_WEEKS),
				period.getStartDate());

			assertThat(actual).isTrue();
		}

		@DisplayName("등록된 종료일이 있으면 false 를 반환한다. ")
		@ParameterizedTest(name = "[{index}] : {0} ~ {1}")
		@CsvSource({
			"2022-08-16, 2022-08-20"
		})
		void fail(@AggregateWith(PeriodAggregator.class) Period period) {
			boolean actual = assembleRepository.existsByProjectIdAndEndAtBetween(project.id(),
				period.startAtWeeksAgo(ASSEMBLE_LIMITS_OF_NUMBER_FOR_WEEKS),
				period.getStartDate());

			assertThat(actual).isFalse();
		}
	}

	@Nested
	@DisplayName("Assemble 등록시")
	class A_create_Assemble_by_period {
		@DisplayName("일정이 겹치지 않으면 조회 결과는 없다.")
		@ParameterizedTest(name = "[{index}] : {0} ~ {1}")
		@CsvSource({
			"2022-07-11, 2022-07-19",
			"2022-08-10, 2022-08-20"
		})
		void ok(@AggregateWith(PeriodAggregator.class) Period period) {
			Optional<Assemble> actual = assembleRepository.findFirstByProjectIdAndStartAtIsLessThanEqualAndEndAtIsGreaterThanEqual(
				project.id(), period.getEndDate(), period.getStartDate());

			assertThat(actual.isEmpty()).isTrue();
		}

		@DisplayName("일정이 겹치면 조회 결과가 있다.")
		@ParameterizedTest(name = "[{index}] : {0} ~ {1}")
		@CsvSource({
			"2022-06-29, 2022-07-01",
			"2022-06-29, 2022-07-05",
			"2022-06-29, 2022-07-10",
			"2022-07-05, 2022-07-11",
			"2022-07-10, 2022-07-11",
			"2022-07-10, 2022-07-20",
		})
		void fail(@AggregateWith(PeriodAggregator.class) Period period) {
			Optional<Assemble> actual = assembleRepository.findFirstByProjectIdAndStartAtIsLessThanEqualAndEndAtIsGreaterThanEqual(
				project.id(), period.getEndDate(), period.getStartDate());
			assertThat(actual.isPresent()).isTrue();
		}
	}
}

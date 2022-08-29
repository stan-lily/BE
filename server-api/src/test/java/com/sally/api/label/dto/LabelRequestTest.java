package com.sally.api.label.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sally.api.label.TextColor;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

class LabelRequestTest {
	private static ValidatorFactory factory;
	private static Validator validator;

	@BeforeAll
	public static void init() {
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@AfterAll
	public static void close() {
		factory.close();
	}

	@ParameterizedTest
	@MethodSource("provideBlank")
	@DisplayName("라벨 입력시 라벨명, 배경색, 폰트컬러는 공백,null 입력시 에러 처리한다.")
	void label_field_create(String name, String description, String backgroundColor, String fontColor) {
		LabelRequest.LabelDto labelCreateDto = new LabelRequest.LabelDto(name, description, backgroundColor, fontColor);

		Set<ConstraintViolation<LabelRequest.LabelDto>> violations = validator.validate(
			labelCreateDto); // 유효하지 않은 경우 violations 값을 가지고 있다.

		assertThat(violations).isNotEmpty();
		violations
			.forEach(error -> {
				assertThat(error.getMessage()).containsAnyOf("라벨명을 입력하세요", "공백일 수 없습니다", "크기가 7에서 7 사이여야 합니다");
			});
	}

	private static Stream<Arguments> provideBlank() {
		return Stream.of(
			Arguments.of(null, null, " ", " "),
			Arguments.of(" ", "nullable", "#FBCA04", TextColor.BRIGHT.name()),
			Arguments.of("name", "nullable", "#FBCA0", TextColor.DARK.name()),
			Arguments.of("123456789012345678901", "", "#FBCA044", TextColor.BRIGHT.name())
		);
	}
}

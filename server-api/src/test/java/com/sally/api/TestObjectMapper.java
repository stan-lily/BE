package com.sally.api;

import com.sally.api.assemble.Assemble;
import com.sally.api.project.Project;

import java.time.LocalDate;
import java.util.List;

public abstract class TestObjectMapper {

	public static final String ASSEMBLE_TEST_FOR_TITLE = "테스트1";

	public static Project project() {
		return Project.crate("테스트 프로젝트", "stan-lily2", 20);
	}

	public static List<Assemble> assemble(Project project) {
		return List.of(
			Assemble.createFrom(ASSEMBLE_TEST_FOR_TITLE, LocalDate.of(2022, 7, 1), LocalDate.of(2022, 7, 10), project),
			Assemble.createFrom("테스트2", LocalDate.of(2022, 7, 20), LocalDate.of(2022, 7, 25), project),
			Assemble.createFrom("테스트3", LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 10), project),
			Assemble.createFrom("테스트4", LocalDate.of(2022, 12, 10), LocalDate.of(2022, 12, 20), project),
			Assemble.createFrom("테스트5", LocalDate.of(2023, 1, 10), LocalDate.of(2023, 1, 20), project));
	}
}

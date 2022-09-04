package com.sally.api.label;

import static org.assertj.core.api.Assertions.assertThat;

import com.sally.api.project.Project;
import com.sally.api.project.ProjectRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class LabelRepositoryTest {
	public static final String TEST_FOR_LABEL_NAME = "\uD83D\uDCDD테스트";
	private final ProjectRepository projectRepository;
	private final LabelRepository labelRepository;

	private Label labelIsDeletedForTest;
	private Project project;
	public Long testProjectId;

	public LabelRepositoryTest(
		@Autowired ProjectRepository projectRepository,
		@Autowired LabelRepository labelRepository) {
		this.projectRepository = projectRepository;
		this.labelRepository = labelRepository;
	}

	@BeforeEach
	void beforeEach() {
		project = projectRepository.saveAndFlush(getProject());
		testProjectId = project.id();
		assertThat(testProjectId).isNotNull();

		labelIsDeletedForTest = getLabel(TEST_FOR_LABEL_NAME + 2, "#77F608");
		labelIsDeletedForTest.delete();

		labelRepository.saveAllAndFlush(List.of(
			getLabel(TEST_FOR_LABEL_NAME, "#FBCA04"),
			labelIsDeletedForTest));
	}

	@Test
	@DisplayName("라벨 db 저장은 삭제 대상이 아닌 조회결과로 확인한다.")
	void save_findAll_isNotDeleted() {
		List<Label> actual = labelRepository.findAllByProjectId(testProjectId);

		assertThat(actual).extracting("name").contains(TEST_FOR_LABEL_NAME);
		assertThat(actual).extracting("isDeleted").contains(false);
		assertThat(actual).extracting("isDeleted").doesNotContain(true);
	}

	@Test
	@DisplayName("라벨 삭제 처리는 isDeleted가 true 로 변경 된 결과값으로 확인한다.")
	void save_delete_deletedTrue() {
		List<Label> actual = labelRepository.findByLabelIsDeletedAAndProject(testProjectId);

		assertThat(actual).extracting("id").contains(labelIsDeletedForTest.id());
		assertThat(actual).extracting("isDeleted").contains(true);
	}

	private Label getLabel(String name, String backgroundColor) {
		System.out.println(testProjectId);
		return Label.createFrom(name, "", backgroundColor, "DARK", testProjectId);
	}

	private Project getProject() {
		return Project.crate("테스트 프로젝트", "stan-lily2", 20);
	}
}

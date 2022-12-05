package com.sally.api.issue;

import static org.assertj.core.api.Assertions.assertThat;

import com.sally.api.issue.infrastructure.IssueRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class IssueRepositoryTest {
	private final IssueRepository issueRepository;

	public IssueRepositoryTest(@Autowired IssueRepository issueRepository) {
		this.issueRepository = issueRepository;
	}

	@Test
	@DisplayName("이슈개수는 project 별로 조회한다.")
	void readIssueCount_eachOfProject_countIssueNoByProjectId() {
		Long actual = issueRepository.countIssueNoByProjectId(1L);

		assertThat(actual).isNotZero();
	}

}

package com.sally.api.issue.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.sally.api.issue.domain.dto.AuthorAndAssignees;
import com.sally.api.issue.domain.dto.IssueRequest;
import com.sally.api.issue.infrastructure.AssigneeRepository;
import com.sally.api.issue.infrastructure.IssueLabelRepository;
import com.sally.api.issue.infrastructure.IssueMilestoneRepository;
import com.sally.api.label.LabelService;
import com.sally.api.milestone.domain.MilestoneReader;
import com.sally.api.project.dto.ProjectInfo;
import com.sally.api.user.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Issue는")
class IssueServiceTest {
	@InjectMocks
	IssueService issueService;

	@Mock
	UserService userService;
	@Mock
	IssueStore issueStore;
	@Mock
	IssueReader issueReader;

	@Mock
	MilestoneReader milestoneReader;
	@Mock
	IssueMilestoneRepository issueMilestoneRepository;
	@Mock
	AssigneeRepository assigneeRepository;
	@Mock
	IssueLabelRepository issueLabelRepository;
	@Mock
	LabelService labelService;

	/**
	 * save() 로직 테스트
	 * - 파라미터에 따른 로직 검증
	 *   - author 필수 -> 없으면 isValidAndGet() 예외 처리
	 *   - milestone 없으면 0으로 저장, 있으면 milestoneId로 저장
	 *   - assignee 없으면 -> assigneeRepository.saveAll() 호출 X
	 *   - label 없으면 -> issueLabelRepository.saveAll() 호출 X
	 *           있으면 -> issueLabelRepository.saveAll() 호출 O
	 * - Issue 필수값들은 Controller에서 검증 후 service 전달하기 때문에, author만 검증되면 Issue 저장 로직 진행
	 */

	@Nested
	@DisplayName("author 필수 파라미터가 있고")
	class Context_with_Author {
		@Mock
		IssueRequest.Registration request;

		@BeforeEach
		void beforeEach() {
			when(request.authorAndAssignees()).thenReturn(new AuthorAndAssignees(1L));
		}

		@Test
		@DisplayName("다른 파라미터들 없어도 저장된다.")
		void issueSave_hasAuthor_processed() {
			when(issueReader.getCurrentIssuerNo(1L)).thenReturn(2L);
			issueService.save(request, new ProjectInfo(1L));

			verify(userService).isValidAndGet(request.authorAndAssignees());
			verifyNoInteractions(milestoneReader);
			verify(issueStore, times(1)).store(any());
		}

		@Test
		@DisplayName("milestone이 있으면 milestone 저장 요청 한다.")
		void issueSave_hasMilestone_byMoreThanZero() {
			Issue issue = Mockito.mock(Issue.class);
			when(request.hasMilestone()).thenReturn(true);
			when(issueStore.store(any())).thenReturn(issue);
			when(milestoneReader.hasId(any(Long.class))).thenReturn(true);

			issueService.save(request, new ProjectInfo(1L));

			verify(request).toEntity(any(), any(), any());
			verify(issueMilestoneRepository).save(any());
		}

		@Test
		@DisplayName("milestone이 없으면 milestone 요청처리하지 않는다.")
		void issueSave_noneOfMilestone_byZero() {
			when(request.hasMilestone()).thenReturn(false);

			issueService.save(request, new ProjectInfo(1L));

			verifyNoInteractions(milestoneReader);
		}

		@Test
		@DisplayName("assignee이 있으면 Assignee를 저장한다.")
		void issueSave_hasAssignees_assigneeRepositorySaveAll() {
			Issue issue = Mockito.mock(Issue.class);
			AuthorAndAssignees assignees = new AuthorAndAssignees(1L, List.of(1L, 2L));
			when(issueStore.store(any())).thenReturn(issue);
			when(request.authorAndAssignees()).thenReturn(assignees);
			when(request.hasAssignees()).thenReturn(true);
			when(issue.id()).thenReturn(1L);

			issueService.save(request, new ProjectInfo(1L));

			verify(assigneeRepository, times(1)).saveAll(any(List.class));
		}

		@Test
		@DisplayName("assignee이 없으면 Assignee를 저장은 진행되지 않는다.")
		void issueSave_noneOfAssignees_noProcessAssigneeRepositorySaveAll() {
			issueService.save(request, new ProjectInfo(1L));

			assertThat(request.hasAssignees()).isFalse();
			verifyNoInteractions(assigneeRepository);
		}

		@Test
		@DisplayName("label이 있으면 IssueLabel을 저장한다.")
		void issueSave_hasLabel_issueLabelRepositorySaveAll() {
			when(request.hasLabels()).thenReturn(true);
			when(request.getLabels()).thenReturn(List.of(1L, 2L));

			issueService.save(request, new ProjectInfo(1L));

			verify(issueLabelRepository, times(1)).saveAll(any(List.class));
		}

		@Test
		@DisplayName("label이 없으면 IssueLabel을 저장하지 않는다.")
		void issueSave_noneOfLabel_noProcessIssueLabelRepositorySaveAll() {
			issueService.save(request, new ProjectInfo(1L));

			assertThat(request.hasLabels()).isFalse();
			verifyNoInteractions(issueLabelRepository);
		}
	}
}

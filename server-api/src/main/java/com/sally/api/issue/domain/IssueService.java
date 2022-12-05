package com.sally.api.issue.domain;

import com.sally.api.issue.domain.dto.AuthorAndAssignees;
import com.sally.api.issue.domain.dto.IssueRequest;
import com.sally.api.issue.domain.dto.IssueResponse;
import com.sally.api.issue.domain.dto.NumberOfIssueStatus;
import com.sally.api.issue.domain.dto.NumberOfIssueStatusInMilestone;
import com.sally.api.issue.infrastructure.AssigneeRepository;
import com.sally.api.issue.infrastructure.IssueLabelRepository;
import com.sally.api.issue.infrastructure.IssueMilestoneRepository;
import com.sally.api.issue.infrastructure.dto.IssueOfMilestoneTitle;
import com.sally.api.issue.infrastructure.dto.MilestoneTitle;
import com.sally.api.label.Label;
import com.sally.api.label.LabelService;
import com.sally.api.milestone.domain.Milestone;
import com.sally.api.milestone.domain.MilestoneReader;
import com.sally.api.project.Project;
import com.sally.api.project.dto.ProjectInfo;
import com.sally.api.user.User;
import com.sally.api.user.UserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IssueService {
	private final IssueReader issueReader;
	private final IssueStore issueStore;
	private final AssigneeRepository assigneeRepository;
	private final IssueLabelRepository issueLabelRepository;
	private final IssueMilestoneRepository issueMilestoneRepository;

	private final UserService userService;
	private final LabelService labelService;
	private final MilestoneReader milestoneReader;

	/**
	 * Issue 등록시 필수값들 외에 연관관계 관련 입력값들이 비필수 일 경우에 맞춘 검증과 저장 로직입니다.
	 * - author필수, assginee null 가능
	 */
	@Transactional
	public void save(IssueRequest.Registration creationDto, ProjectInfo project) {
		AuthorAndAssignees authorAndAssignees = creationDto.authorAndAssignees();
		userService.isValidAndGet(authorAndAssignees);

		// FK 전달 위해 이슈 등록 후 assignee,labels 등록
		Issue issue = issueStore.store(
			creationDto.toEntity(
				User.from(authorAndAssignees.author()),
				Project.from(project.getId()),
				getNextIssueNo(project)));

		registerMilestoneOrLabel(creationDto, issue);
		registerAssignee(creationDto, project, issue);
		// TODO IssueLabelService 에서 labelService 의존성을 갖고 Issue에는 LabelService 의존성이 없다면 ? - 노션 정리 -> 리팩토링 이슈 추가
	}

	/**
	 * 이슈 - 제목, 이슈순서번호, 상태, 작성일
	 *   + 작성자 - 작성자명, 작성자 이미지 (1:1)
	 *   + 라벨 - 라벨명 (1:N or null)
	 */
	@Transactional(readOnly = true)
	public IssueResponse.ListOfOpened readAllOpenStatus(ProjectInfo project) {
		// 이슈별 열린상태 개수, 닫힌 상태 개수 - 팀전체 이슈 상태별 개수 - 1번만
		List<NumberOfIssueStatus> issueStatusCount = issueReader.getIssueStatusCount(project.getId());

		List<Issue> issues = issueReader.getAllIssueAndStatus(project.getId(), Status.OPEN);

		//  마일스톤 - 마일스톤 제목 (1:1 or null)
		MilestoneTitle milestoneTitle = issueMilestoneRepository.findAllByIssueIdIn(getIssueIdsFrom(issues));
		IssueOfMilestoneTitle mapper = milestoneTitle.getTitleOfMilestoneWithIssueId();

		return new IssueResponse.ListOfOpened(
			getEachOfCountFrom(issueStatusCount),
			issues.stream()
				.map(issue -> IssueResponse.IssueAndLabelAndMilestone.of(issue.toIssueInfo(),
					mapper.getMilestoneTitleFrom(issue.id())))
				.collect(Collectors.toUnmodifiableList()));
	}

	public void registerMilestoneOrLabel(IssueRequest.Registration creationDto, Issue issue) {
		if (creationDto.hasMilestone()) {
			registerMilestone(creationDto, issue);
		}
		if (creationDto.hasLabels()) {
			registerLabel(creationDto, issue);
		}
	}

	public void registerLabel(IssueRequest.Registration creationDto, Issue issue) {
		List<Label> labels = labelService.readAllByIds(creationDto.getLabels());
		issueLabelRepository.saveAll(labels.stream()
			.map(label -> new IssueLabel(issue, label))
			.collect(Collectors.toUnmodifiableList()));
	}

	public void registerMilestone(IssueRequest.Registration creationDto, Issue issue) {
		if (!milestoneReader.hasId(creationDto.getMilestone())) {
			throw new IllegalArgumentException("no exist of milestone-id when issue register");
		}
		issueMilestoneRepository.save(new IssueMilestone(Milestone.from(creationDto.getMilestone()), issue));
	}

	public void registerAssignee(IssueRequest.Registration creationDto, ProjectInfo project, Issue issue) {
		if (creationDto.hasAssignees()) {
			AuthorAndAssignees authorAndAssignees = creationDto.authorAndAssignees();
			assigneeRepository.saveAll(
				authorAndAssignees.assignees()
					.stream()
					.map(assigneeId -> new AssigneeId(assigneeId, project.getId(), issue.id()))
					.map(Assignee::new)
					.collect(Collectors.toUnmodifiableList()));
		}
	}

	private Long getNextIssueNo(ProjectInfo project) {
		return issueReader.getCurrentIssuerNo(project.getId()) + 1L;
	}

	public List<NumberOfIssueStatusInMilestone> getIssueStatusCountWithMilestone(Long projectId) {
		return issueMilestoneRepository.findAllByIssueStatusAndCountGroupByIssueWithMilestone(projectId);
	}

	private IssueResponse.IssueStatusCount getEachOfCountFrom(List<NumberOfIssueStatus> issueStatusCount) {
		long openCount = 0;
		long closedCount = 0;
		for (NumberOfIssueStatus issueStatus : issueStatusCount) {
			if (Status.OPEN.equals(issueStatus.getIssueStatus())) {
				openCount = issueStatus.getStatusCount();
			} else {
				closedCount = issueStatus.getStatusCount();
			}
		}
		return new IssueResponse.IssueStatusCount(openCount, closedCount);
	}

	private List<Long> getIssueIdsFrom(List<Issue> issues) {
		return issues.stream().map(Issue::id).collect(Collectors.toList());
	}
}

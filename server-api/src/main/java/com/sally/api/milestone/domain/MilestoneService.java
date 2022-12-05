package com.sally.api.milestone.domain;

import com.sally.api.issue.domain.IssueService;
import com.sally.api.issue.domain.dto.NumberOfIssueStatusInMilestone;
import com.sally.api.milestone.domain.dto.MilestoneOfIssueStatusCount;
import com.sally.api.milestone.domain.dto.MilestoneRequest;
import com.sally.api.milestone.domain.dto.MilestoneResponse;
import com.sally.api.project.dto.ProjectInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MilestoneService {
	private final MilestoneStore milestoneStore;
	private final MilestoneReader milestoneReader;
	private final IssueService issueService;

	@Transactional
	public void create(MilestoneRequest.CreationDto milestoneCreationRequest, ProjectInfo project) {
		final Milestone milestone = milestoneCreationRequest.toEntity(project);
		milestoneStore.store(milestone);
	}

	@Transactional(readOnly = true)
	public MilestoneResponse.Lists readAllByTeam(ProjectInfo project) {
		MilestoneOfIssueStatusCount mapper = toMilestoneStatusCountMapper(issueService.getIssueStatusCountWithMilestone(
			project.getId()));
		return new MilestoneResponse.Lists(
			milestoneReader.readAllFrom(project.getId())
				.stream()
				.map(milestone -> {
					return new MilestoneResponse.MilestoneDto(
						milestone.id(),
						milestone.title(),
						milestone.description(),
						milestone.startDate(),
						milestone.completionDate(),
						mapper.getOpenCountOrZero(milestone.id()),
						mapper.getCloseCountOrZero(milestone.id()));
				})
				.collect(Collectors.toUnmodifiableList()));
	}

	public MilestoneOfIssueStatusCount toMilestoneStatusCountMapper(
		List<NumberOfIssueStatusInMilestone> issueCounts) {
		MilestoneOfIssueStatusCount statusCount = new MilestoneOfIssueStatusCount();
		issueCounts.stream()
			.forEach(statusCount::toMilestoneCountInfo);
		return statusCount;
	}
}

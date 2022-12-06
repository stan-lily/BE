package com.sally.api.issue.infrastructure;

import com.sally.api.issue.domain.Issue;
import com.sally.api.issue.domain.IssueReader;
import com.sally.api.issue.domain.Status;
import com.sally.api.issue.domain.dto.NumberOfIssueStatus;

import org.springframework.stereotype.Component;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class IssueReaderImpl implements IssueReader {
	private final IssueRepository issueRepository;

/*	@Override
	public List<NumberOfIssueStatusInMilestone> getIssueStatusCountWithMilestone(Long projectId) {
		return issueRepository.findIssueStatusAndCountGroupByIssueWithMilestone(projectId);
	}*/

	@Override
	public List<NumberOfIssueStatus> getIssueStatusCount(Long projectId) {
		return issueRepository.findIssueStatusAndCountGroupByIssue(projectId);
	}

	@Override
	public List<Issue> getAllIssueAndStatus(Long projectId, Status status) {
		return issueRepository.findAllByProjectIdAndStatus(projectId, status);
	}

	@Override
	public Long getCurrentIssuerNo(Long projectId) {
		return issueRepository.countIssueNoByProjectId(projectId);
	}
}

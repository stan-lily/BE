package com.sally.api.issue.domain.dto;

public interface NumberOfIssueStatusInMilestone {
	String getIssueStatus();

	Long getStatusCount();

	Long getMilestoneId();
}

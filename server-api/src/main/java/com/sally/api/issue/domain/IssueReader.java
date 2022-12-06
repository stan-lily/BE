package com.sally.api.issue.domain;

import com.sally.api.issue.domain.dto.NumberOfIssueStatus;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueReader {
	List<NumberOfIssueStatus> getIssueStatusCount(@Param("projectId") Long projectId);

	List<Issue> getAllIssueAndStatus(Long projectId, Status status);

	Long getCurrentIssuerNo(Long projectId);
}

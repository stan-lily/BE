package com.sally.api.issue.infrastructure;

import com.sally.api.issue.domain.Issue;
import com.sally.api.issue.domain.Status;
import com.sally.api.issue.domain.dto.NumberOfIssueStatus;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
	@EntityGraph(value = "Issue.all", type = EntityGraph.EntityGraphType.LOAD)
	List<Issue> findAllByProjectIdAndStatus(Long projectId, Status status);

	@Query(value =
		"select status as issueStatus, count(status) as statusCount"
			+ " from tb_issue"
			+ " where project_id = :projectId and is_deleted = false"
			+ " group by status"
		, nativeQuery = true
	)
	List<NumberOfIssueStatus> findIssueStatusAndCountGroupByIssue(@Param("projectId") Long projectId);

	Long countIssueNoByProjectId(@Param("projectId") Long projectId);
}

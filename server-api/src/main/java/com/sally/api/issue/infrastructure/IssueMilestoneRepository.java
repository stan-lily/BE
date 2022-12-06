package com.sally.api.issue.infrastructure;

import com.sally.api.issue.domain.IssueMilestone;
import com.sally.api.issue.domain.dto.NumberOfIssueStatusInMilestone;
import com.sally.api.issue.infrastructure.dto.MilestoneTitle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueMilestoneRepository extends JpaRepository<IssueMilestone, Long> {
	@Query("select DISTINCT a from IssueMilestone as a join fetch a.issue as i join fetch a.milestone as m where i.id in (:issuesIds)")
	MilestoneTitle findAllByIssueIdIn(@Param("issuesIds") List<Long> issuesIds);
	
	@Query(value =
		"select distinct b.status as issueStatus,"
			+ " count(b.status) as statusCount,"
			+ " a.milestone_id as milestoneId"
			+ " from tb_milestone_has_issue as a"
			+ " inner join tb_issue as b on a.issue_id = b.id"
			+ " inner join tb_milestone as c on a.milestone_id = c.id"
			+ " where b.project_id = :projectId"
			+ " group by b.status, a.milestone_id ;"
		, nativeQuery = true)
	List<NumberOfIssueStatusInMilestone> findAllByIssueStatusAndCountGroupByIssueWithMilestone(
		@Param("projectId") Long projectId);

}

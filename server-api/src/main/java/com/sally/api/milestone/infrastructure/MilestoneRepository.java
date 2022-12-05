package com.sally.api.milestone.infrastructure;

import com.sally.api.milestone.domain.Milestone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
	boolean existsById(@Param("milestoneId") Long milestoneId);

	List<Milestone> findAllByProjectId(@Param("projectId") Long projectId);
}

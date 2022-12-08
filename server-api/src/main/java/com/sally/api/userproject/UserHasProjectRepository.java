package com.sally.api.userproject;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserHasProjectRepository extends JpaRepository<UserHasProject, Long> {
	@EntityGraph(value = "UserHasProject.all", type = EntityGraph.EntityGraphType.FETCH)
	List<UserHasProject> findAllByIdIn(@Param("userHasProjectIds") List<UserHasProjectId> userHasProjectIds);
}

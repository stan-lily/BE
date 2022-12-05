package com.sally.api.milestone.interfaces;

import com.sally.api.login.dto.FakeAuthUser;
import com.sally.api.milestone.domain.MilestoneService;
import com.sally.api.milestone.domain.dto.MilestoneRequest;
import com.sally.api.milestone.domain.dto.MilestoneResponse;
import com.sally.api.project.ProjectService;
import com.sally.api.project.dto.ProjectInfo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/{team-name}/issue-tracker/milestones")
@RestController
public class MilestoneApiController {
	private final MilestoneService milestoneService;
	private final ProjectService projectService;
	private final FakeAuthUser authUser;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create(
		@Valid @RequestBody MilestoneRequest.CreationDto milestoneCreationRequest,
		@PathVariable("team-name") String teamName) {
		ProjectInfo project = projectService.getAndVerify(authUser.projectId(), teamName);
		milestoneService.create(milestoneCreationRequest, project);
	}

	@GetMapping
	public MilestoneResponse.Lists readAll(@PathVariable("team-name") String teamName) {
		ProjectInfo project = projectService.getAndVerify(authUser.projectId(), teamName);
		return milestoneService.readAllByTeam(project);
	}
}

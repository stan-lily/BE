package com.sally.api.issue.interfaces;

import com.sally.api.issue.domain.IssueService;
import com.sally.api.issue.domain.dto.IssueRequest;
import com.sally.api.issue.domain.dto.IssueResponse;
import com.sally.api.login.dto.FakeAuthUser;
import com.sally.api.project.ProjectService;
import com.sally.api.project.dto.ProjectInfo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/{team-name}/issue-tracker/issues")
public class IssueApiController {
	private final IssueService issueService;
	private final ProjectService projectService;
	private final FakeAuthUser authUser;

	@PostMapping
	public void register(@Valid @RequestBody IssueRequest.Registration creationDto,
		@PathVariable("team-name") String teamName) {
		ProjectInfo project = projectService.getFromTeamName(teamName);
		issueService.save(creationDto, project);
	}

	@GetMapping
	public IssueResponse.ListOfOpened readAllOpenIssue(@PathVariable("team-name") String teamName) {
		ProjectInfo project = projectService.getAndVerify(authUser.projectId(), teamName);
		return issueService.readAllOpenStatus(project);
	}
}

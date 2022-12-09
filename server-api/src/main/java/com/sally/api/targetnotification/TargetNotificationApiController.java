package com.sally.api.targetnotification;

import com.sally.api.login.dto.FakeAuthUser;
import com.sally.api.project.ProjectService;
import com.sally.api.project.dto.ProjectInfo;
import com.sally.api.targetnotification.dto.TargetNotificationRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/{team-name}/assembles/targets/delays")
public class TargetNotificationApiController {
	private final TargetNotificationService targetNotificationService;
	private final ProjectService projectService;
	private final FakeAuthUser authUser;

	@PostMapping
	public void registerDelayedTarget(
		@Valid @RequestBody TargetNotificationRequest.registration request,
		@PathVariable("team-name") String teamName) {
		ProjectInfo project = projectService.getAndVerify(authUser.projectId(), teamName);
		targetNotificationService.create(request, project, authUser);
	}
}

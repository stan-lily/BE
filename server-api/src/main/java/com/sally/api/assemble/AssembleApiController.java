package com.sally.api.assemble;

import com.sally.api.assemble.dto.AssembleRequest;
import com.sally.api.assemble.dto.AssembleResponse;
import com.sally.api.login.dto.FakeAuthUser;
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

import java.util.List;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/{team-name}/asembles")
@RestController
public class AssembleApiController {
	private final AssembleService assembleService;
	private final ProjectService projectService;
	private final FakeAuthUser authUser;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create(
		@Valid @RequestBody AssembleRequest.CreationDto creationDto,
		@PathVariable("team-name") String teamName) {
		ProjectInfo project = projectService.getAndVerify(authUser.projectId(), teamName);
		assembleService.create(creationDto, project);
	}

	@GetMapping
	public List<AssembleResponse.AssembleDto> readAll(
		@PathVariable("team-name") String teamName) {
		ProjectInfo project = projectService.getAndVerify(authUser.projectId(), teamName);
		return assembleService.readAll(project);
	}
}

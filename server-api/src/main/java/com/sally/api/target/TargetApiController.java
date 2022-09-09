package com.sally.api.target;

import com.sally.api.login.dto.FakeAuthUser;
import com.sally.api.project.ProjectService;
import com.sally.api.project.dto.ProjectInfo;
import com.sally.api.target.dto.TargetRequest;
import com.sally.api.target.dto.TargetResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/{team-name}/assembles/{id}")
@RestController
public class TargetApiController {
	private final ProjectService projectService;
	private final TargetService targetService;
	private final FakeAuthUser authUser;

	@PostMapping("/targets")
	@ResponseStatus(HttpStatus.CREATED)
	public void register(
		@Valid @RequestBody TargetRequest.CreationDto creationDto,
		@PathVariable("team-name") String teamName,
		@PathVariable("id") Long assembleId) {
		ProjectInfo project = projectService.getAndVerify(authUser.projectId(), teamName);
		targetService.save(assembleId, project, creationDto);
	}

	@GetMapping
	public TargetResponse.TargetCountsDto getTargetCountByPeriod(
		@PathVariable("team-name") String teamName,
		@PathVariable("id") Long assembleId) {
		projectService.getAndVerify(authUser.projectId(), teamName);
		return targetService.readTargetCountsByPeriod(assembleId);
	}

	@GetMapping("/targets")
	public TargetResponse.TargetDto getTargetByDay(
		@PathVariable("team-name") String teamName,
		@PathVariable("id") Long assembleId,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		projectService.getAndVerify(authUser.projectId(), teamName);
		return targetService.readTargetsByDay(assembleId, date);
	}
}

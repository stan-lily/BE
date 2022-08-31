package com.sally.api.label;

import com.sally.api.label.dto.LabelRequest;
import com.sally.api.label.dto.LabelResponse;
import com.sally.api.login.dto.FakeAuthUser;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/{team-name}/labels")
@RestController
public class LabelApiController {
	private final LabelService labelService;
	private final FakeAuthUser authUser;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create(
		@Valid @RequestBody LabelRequest.LabelDto labelDtoRequest,
		@PathVariable("team-name") String teamName) {
		labelService.create(labelDtoRequest, teamName, authUser);
	}

	@GetMapping
	public LabelResponse.Lists readAll(@PathVariable("team-name") String teamName) {
		return labelService.readAll(teamName);
	}

	@PutMapping("/{id}")
	public void modify(
		@PathVariable(name = "id") Long labelId,
		@Valid @RequestBody LabelRequest.LabelDto labelDtoRequest,
		@PathVariable("team-name") String teamName) {
		labelService.update(labelId, labelDtoRequest, teamName, authUser);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(name = "id") Long labelId, @PathVariable("team-name") String teamName) {
		labelService.delete(labelId, teamName, authUser);
	}
}

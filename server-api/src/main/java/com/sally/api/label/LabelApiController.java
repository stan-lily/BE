package com.sally.api.label;

import com.sally.api.label.dto.LabelRequest;
import com.sally.api.login.dto.FakeAuthUser;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/{team-name}/labels/")
@RestController
public class LabelApiController {
	private final LabelService labelService;
	private final FakeAuthUser authUser;

	@PostMapping
	public ResponseEntity<Void> create(
		@Valid @RequestBody LabelRequest.Creation creationRequest,
		@PathVariable("team-name") String teamName) {
		labelService.create(creationRequest, teamName, authUser);
		return ResponseEntity.ok().build();
	}
}

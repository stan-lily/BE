package com.sally.api.label;

import com.sally.api.label.dto.LabelRequest;
import com.sally.api.login.dto.AuthUser;
import com.sally.api.project.ProjectService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LabelService {
	private final LabelRepository labelRepository;
	private final ProjectService projectService;

	public void create(LabelRequest.Creation creation, String teamName, AuthUser authUser) {
		Label label = creation.toEntity(projectService.getAndVerify(authUser.projectId(), teamName));
		labelRepository.save(label);
	}
}
package com.sally.api.label;

import com.sally.api.label.dto.LabelRequest;
import com.sally.api.label.dto.LabelResponse;
import com.sally.api.login.dto.AuthUser;
import com.sally.api.login.dto.FakeAuthUser;
import com.sally.api.project.Project;
import com.sally.api.project.ProjectService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LabelService {
	private final LabelRepository labelRepository;
	private final ProjectService projectService;

	@Transactional
	public void create(LabelRequest.LabelDto labelDto, String teamName, AuthUser authUser) {
		if (labelRepository.existsByName(labelDto.getName())) {
			throw new RuntimeException("Overlap : label");
		}
		Label label = labelDto.toEntity(getProjectAndVerify(teamName, authUser));
		labelRepository.save(label);
	}

	@Transactional(readOnly = true)
	public LabelResponse.Lists readAll(String teamName, FakeAuthUser authUser) {
		Project project = getProjectAndVerify(teamName, authUser);
		return new LabelResponse.Lists(
			labelRepository.findAllByProjectId(project.id()).stream()
				.map(LabelResponse.LabelDto::new)
				.collect(Collectors.toList()));
	}

	@Transactional
	public void update(Long labelId, LabelRequest.LabelDto labelDto, String teamName,
		FakeAuthUser authUser) {
		Project project = getProjectAndVerify(teamName, authUser);
		Label label = getLabelOrThrow(labelId, project);
		label.update(
			labelDto.getName(),
			labelDto.getDescription(),
			labelDto.getBackgroundColor(),
			labelDto.getFontColor());
	}

	@Transactional
	public void delete(Long labelId, String teamName, AuthUser authUser) {
		Project project = getProjectAndVerify(teamName, authUser);
		Label label = getLabelOrThrow(labelId, project);
		label.delete();
	}

	private Label getLabelOrThrow(Long labelId, Project project) {
		return labelRepository.findByIdAndProjectId(labelId, project.id())
			.orElseThrow(() -> new NoSuchElementException("no existed label"));
	}

	private Project getProjectAndVerify(String teamName, AuthUser authUser) {
		return projectService.getAndVerify(authUser.projectId(), teamName);
	}
}

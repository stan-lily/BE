package com.sally.api.project;

import com.sally.api.project.dto.ProjectInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectService {
	private final ProjectRepository projectRepository;

	@Transactional(readOnly = true)
	public ProjectInfo getAndVerify(Long projectId, String teamName) {
		Project project = get(projectId);
		if (project.have(teamName)) {
			return new ProjectInfo(project.id());
		}
		throw new RuntimeException("unauthorized access to project");
	}

	@Transactional(readOnly = true)
	public Project get(Long projectId) {
		return projectRepository.findById(projectId)
			.orElseThrow(() -> new NoSuchElementException("no project entity"));
	}

	public ProjectInfo getFromTeamName(String teamName) {
		Optional<Project> project = projectRepository.findByPath(teamName);
		if (project.isEmpty()) {
			throw new NoSuchElementException("no project entity");
		}
		return new ProjectInfo(project.get().id());
	}
}

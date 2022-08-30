package com.sally.api.project;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectService {
	private final ProjectRepository projectRepository;

	@Transactional(readOnly = true)
	public Project getAndVerify(Long projectId, String teamName) {
		Project project = get(projectId);
		if (project.have(teamName)) {
			return project;
		}
		throw new RuntimeException("unauthorized access to project");
	}

	@Transactional(readOnly = true)
	public Project get(Long projectId) {
		return projectRepository.findById(projectId)
			.orElseThrow(() -> new NoSuchElementException("no project entity"));
	}
}

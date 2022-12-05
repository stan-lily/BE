package com.sally.api.milestone.infrastructure;

import com.sally.api.milestone.domain.Milestone;
import com.sally.api.milestone.domain.MilestoneReader;

import org.springframework.stereotype.Component;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MilestoneReaderImpl implements MilestoneReader {
	private final MilestoneRepository milestoneRepository;

	public List<Milestone> readAllFrom(Long projectId) {
		return milestoneRepository.findAllByProjectId(projectId);
	}

	@Override
	public boolean hasId(Long milestoneId) {
		return milestoneRepository.existsById(milestoneId);
	}
}

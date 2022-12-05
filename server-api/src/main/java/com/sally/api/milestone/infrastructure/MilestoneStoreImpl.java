package com.sally.api.milestone.infrastructure;

import com.sally.api.milestone.domain.Milestone;
import com.sally.api.milestone.domain.MilestoneStore;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MilestoneStoreImpl implements MilestoneStore {
	private final MilestoneRepository milestoneRepository;

	@Override
	public Milestone store(Milestone milestone) {
		return milestoneRepository.save(milestone);
	}
}

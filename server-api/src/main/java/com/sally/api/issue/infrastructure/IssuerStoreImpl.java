package com.sally.api.issue.infrastructure;

import com.sally.api.issue.domain.Issue;
import com.sally.api.issue.domain.IssueStore;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class IssuerStoreImpl implements IssueStore {
	private final IssueRepository issueRepository;

	@Override
	public Issue store(Issue issue) {
		return issueRepository.save(issue);
	}
}

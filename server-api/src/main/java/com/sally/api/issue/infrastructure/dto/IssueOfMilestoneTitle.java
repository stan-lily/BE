package com.sally.api.issue.infrastructure.dto;

import java.util.Map;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IssueOfMilestoneTitle {
	private final Map<Long, String> mapper;

	public String getMilestoneTitleFrom(Long issueId) {
		return this.mapper.containsKey(issueId) ? this.mapper.get(issueId) : "";
	}
}

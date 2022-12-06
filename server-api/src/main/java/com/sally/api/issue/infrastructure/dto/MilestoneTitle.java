package com.sally.api.issue.infrastructure.dto;

import com.sally.api.issue.domain.IssueMilestone;

import org.springframework.data.util.Streamable;

import java.util.Iterator;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor(staticName = "of")
public class MilestoneTitle implements Streamable<IssueMilestone> {
	private final Streamable<IssueMilestone> streamable;

	public IssueOfMilestoneTitle getTitleOfMilestoneWithIssueId() {
		return new IssueOfMilestoneTitle(streamable.stream()
			.collect(Collectors.toMap(IssueMilestone::issueId, IssueMilestone::milestoneTitle)));
	}

	@Override
	public Iterator<IssueMilestone> iterator() {
		return stream().iterator();
	}
}

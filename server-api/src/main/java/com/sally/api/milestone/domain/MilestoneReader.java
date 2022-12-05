package com.sally.api.milestone.domain;

import java.util.List;

public interface MilestoneReader {
	List<Milestone> readAllFrom(Long projectId);

	boolean hasId(Long milestone);
}
// List<IssueOfMilestoneTitle> getMilestoneTitleWithIssueIds(List<Long> issueIds);

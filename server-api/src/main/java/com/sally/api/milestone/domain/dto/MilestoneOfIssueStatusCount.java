package com.sally.api.milestone.domain.dto;

import com.sally.api.issue.domain.dto.NumberOfIssueStatusInMilestone;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MilestoneOfIssueStatusCount {
	private final Map<Long, StatusCount> milestoneMapper = new ConcurrentHashMap<>();

	public void toMilestoneCountInfo(NumberOfIssueStatusInMilestone issueStatus) {
		Long milestoneId = issueStatus.getMilestoneId();
		if (!milestoneMapper.containsKey(milestoneId)) {
			milestoneMapper.put(milestoneId, new StatusCount());
		}
		if (issueStatus.getIssueStatus().equals("OPEN")) {
			milestoneMapper.put(
				milestoneId,
				new StatusCount(issueStatus.getStatusCount(), milestoneMapper.get(milestoneId).getClosedCount()));
		} else {
			milestoneMapper.put(
				milestoneId,
				new StatusCount(milestoneMapper.get(milestoneId).getOpenCount(), issueStatus.getStatusCount()));
		}
	}

	public Long getOpenCountOrZero(Long milestoneId) {
		return this.milestoneMapper.containsKey(milestoneId) ? this.milestoneMapper.get(milestoneId).getOpenCount() :
			0L;
	}

	public Long getCloseCountOrZero(Long milestoneId) {
		return this.milestoneMapper.containsKey(milestoneId) ? this.milestoneMapper.get(milestoneId).getClosedCount() :
			0L;
	}

	@NoArgsConstructor
	public static class StatusCount {
		private Long openCount = 0L;
		private Long closedCount = 0L;

		public StatusCount(Long openCount, Long closedCount) {
			this.openCount = openCount;
			this.closedCount = closedCount;
		}

		public Long getOpenCount() {
			return Objects.isNull(openCount) ? 0L : openCount;
		}

		public Long getClosedCount() {
			return Objects.isNull(closedCount) ? 0L : closedCount;
		}
	}
}



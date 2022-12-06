package com.sally.api.issue.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sally.api.issue.domain.Issue;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class IssueResponse {
	@Getter
	@NoArgsConstructor
	public static class ListOfOpened {
		private IssueStatusCount statusCount;
		private List<IssueAndLabelAndMilestone> issueListOpened;

		public ListOfOpened(
			IssueStatusCount statusCount,
			List<IssueAndLabelAndMilestone> issueListOpened) {
			this.statusCount = statusCount;
			this.issueListOpened = issueListOpened;
		}
	}

	@Getter
	@Builder
	public static class IssueAndLabelAndMilestone {
		private String issueTitle;
		private Long issueNo;
		private String issueStatus;
		@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
		private LocalDateTime createdAt;
		private String author;
		private String imageOfAuthor;
		private String milestoneTitle;
		private List<IssueLabelDto> labels;

		public static IssueAndLabelAndMilestone of(Issue.IssueInfo issueInfo, String milestoneTitle) {
			return IssueAndLabelAndMilestone.builder()
				.issueTitle(issueInfo.getIssueTitle())
				.issueNo(issueInfo.getIssueNo())
				.issueStatus(issueInfo.getStatus())
				.createdAt(issueInfo.getCreatedAt())
				.author(issueInfo.getAuthor())
				.imageOfAuthor(issueInfo.getPicture())
				.milestoneTitle(milestoneTitle)
				.labels(issueInfo.getIssueLabels())
				.build();
		}
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class IssueStatusCount {
		private Long openStatusCount;
		private Long closeStatusCount;

		public static IssueStatusCount of(Long openStatusCount, Long closeStatusCount) {
			return new IssueStatusCount(openStatusCount, closeStatusCount);
		}
	}
}

package com.sally.api.issue.domain.dto;

import com.sally.api.issue.domain.Issue;
import com.sally.api.issue.domain.Status;
import com.sally.api.project.Project;
import com.sally.api.user.User;

import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class IssueRequest {
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Registration {
		@NotBlank
		@Length(max = 50)
		private String title;
		@Length(max = 100)
		private String content;
		@NotBlank
		private String issueStatus;
		private Long milestone;
		private List<Long> labels;
		private List<Long> assignees;
		@NotNull
		@Min(1)
		private Long author;

		public AuthorAndAssignees authorAndAssignees() {
			if (!Objects.isNull(assignees)) {
				return new AuthorAndAssignees(author, assignees);
			}
			return new AuthorAndAssignees(author);
		}

		public Issue toEntity(User writer, Project project, Long issueNo) {
			return Issue.createOf(title, content, issueNo, Status.of(issueStatus), writer, project);
		}

		public boolean hasAssignees() {
			return (!Objects.isNull(assignees) && assignees.size() > 0);
		}

		public boolean hasMilestone() {
			return (!Objects.isNull(milestone) && milestone > 0);
		}

		public boolean hasLabels() {
			return (!Objects.isNull(labels) && labels.size() > 0);
		}
	}
}

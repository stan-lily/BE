package com.sally.api.issue.domain;

import com.sally.api.milestone.domain.Milestone;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Table(name = "tb_milestone_has_issue")
@Entity
public class IssueMilestone {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Milestone milestone;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "issue_id")
	private Issue issue;

	public IssueMilestone(Milestone milestone, Issue issue) {
		this.milestone = milestone;
		this.issue = issue;
	}

	public String milestoneTitle() {
		return milestone.title();
	}

	public Long issueId() {
		return issue.id();
	}
}

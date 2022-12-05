package com.sally.api.milestone.domain;

import com.sally.api.project.Project;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Where(clause = "is_deleted = false")
@Table(name = "tb_milestone")
@Entity
public class Milestone {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "milestone_title")
	private String title;
	private String description;
	private LocalDate startAt;
	private LocalDate endAt;
	@ColumnDefault("0")
	private boolean isDeleted = false;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private Project project;

	@Builder(access = AccessLevel.PRIVATE)
	protected Milestone(
		Long id,
		String title,
		String description,
		LocalDate startAt,
		LocalDate endAt,
		boolean isDeleted,
		Project project) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.startAt = startAt;
		this.endAt = endAt;
		this.isDeleted = isDeleted;
		this.project = project;
	}

	public static Milestone of(String title, String description, LocalDate startAt, LocalDate endAt, Project project) {
		return Milestone.builder()
			.title(title)
			.description(description)
			.startAt(startAt)
			.endAt(endAt)
			.project(project)
			.build();
	}

	public static Milestone from(Long milestoneId) {
		return Milestone.builder()
			.id(milestoneId)
			.build();
	}

	public Long id() {
		return this.id;
	}

	public String title() {
		return this.title;
	}

	public String description() {
		return this.description;
	}

	public LocalDate startDate() {
		return this.startAt;
	}

	public LocalDate completionDate() {
		return this.endAt;
	}
}

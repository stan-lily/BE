package com.sally.api.assemble;

import com.sally.api.project.Project;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import lombok.ToString;

@ToString(exclude = "project")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Table(name = "tb_assemble_day")
@Entity
public class Assemble {
	public static final long ASSEMBLE_LIMITS_OF_NUMBER_FOR_WEEKS = 3L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private LocalDate startAt;
	private LocalDate endAt;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "project_id", referencedColumnName = "id")
	private Project project;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@Builder(access = AccessLevel.PRIVATE)
	public Assemble(
		Long id,
		String title,
		LocalDate startAt,
		LocalDate endAt,
		Project project,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.id = id;
		this.title = title;
		this.startAt = startAt;
		this.endAt = endAt;
		this.project = project;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static Assemble createFrom(String title, LocalDate startAt, LocalDate endAt, Project project) {
		return Assemble.builder()
			.title(title)
			.startAt(startAt)
			.endAt(endAt)
			.project(project)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}

	public static Assemble from(Long id) {
		return Assemble.builder()
			.id(id)
			.build();
	}

	public Long id() {
		return this.id;
	}

	public String title() {
		return this.title;
	}

	public LocalDate startAt() {
		return this.startAt;
	}

	public LocalDate endAt() {
		return this.endAt;
	}
}


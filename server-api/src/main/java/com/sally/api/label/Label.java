package com.sally.api.label;

import com.sally.api.project.Project;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "tb_label")
@Entity
public class Label {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "label_name")
	private String name;
	private String description;
	private String backgroundColor;
	@Enumerated(EnumType.STRING)
	private TextColor fontColor;
	@ColumnDefault("0")
	private boolean isDeleted = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", referencedColumnName = "id")
	private Project project;

	@Builder(access = AccessLevel.PRIVATE)
	public Label(
		Long id,
		String name,
		String description,
		String backgroundColor,
		TextColor fontColor,
		boolean isDeleted,
		Project project) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.backgroundColor = backgroundColor;
		this.fontColor = fontColor;
		this.isDeleted = isDeleted;
		this.project = project;
	}

	public static Label from(Long id) {
		return Label.builder()
			.id(id)
			.build();
	}

	public static Label createFrom(String name, String description, String backgroundColor, String fontColor,
		Long projectId) {
		return Label.builder()
			.name(name)
			.description(description)
			.backgroundColor(backgroundColor)
			.fontColor(TextColor.from(fontColor))
			.project(Project.from(projectId))
			.build();
	}

	public Long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String description() {
		return description;
	}

	public String backgroundColor() {
		return backgroundColor;
	}

	public String fontColorByText() {
		return fontColor.name();
	}

	public void update(String name, String description, String backgroundColor, String fontColor) {
		this.name = name;
		this.description = description;
		this.backgroundColor = backgroundColor;
		this.fontColor = TextColor.from(fontColor);
	}

	public void delete() {
		this.isDeleted = true;
	}
}

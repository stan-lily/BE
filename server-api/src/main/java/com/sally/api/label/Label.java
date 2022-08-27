package com.sally.api.label;

import com.sally.api.project.Project;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	private TextColor fontColor;
	@ColumnDefault("0")
	private boolean isDeleted = false;

	@ManyToOne(fetch = FetchType.LAZY)
	private Project project;

	@Builder
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
}

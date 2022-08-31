package com.sally.api.project;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Where(clause = "is_deleted = false")
@Table(name = "tb_project")
@Entity
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String path;
	private int maxNumberPeople;
	private String invitationAddress;
	private LocalDateTime invitationCreateAt;
	private LocalDateTime createdAt;
	@ColumnDefault("0")
	private boolean isDeleted = false;

	@Builder
	protected Project(Long id, String title, String path, int maxNumberPeople, String invitationAddress,
		LocalDateTime invitationCreateAt, LocalDateTime createdAt, boolean isDeleted) {
		this.id = id;
		this.title = title;
		this.path = path;
		this.maxNumberPeople = maxNumberPeople;
		this.invitationAddress = invitationAddress;
		this.invitationCreateAt = invitationCreateAt;
		this.createdAt = createdAt;
		this.isDeleted = isDeleted;
	}

	public static Project from(Long id) {
		return Project.builder()
			.id(id)
			.build();
	}

	public static Project crate(String title, String path, int maxNumberPeople) {
		return Project.builder()
			.title(title)
			.path(path)
			.maxNumberPeople(maxNumberPeople)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public boolean have(String path) {
		return this.path.equals(path);
	}

	public Long id() {
		return this.id;
	}
}

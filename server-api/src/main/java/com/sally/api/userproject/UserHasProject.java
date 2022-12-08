package com.sally.api.userproject;

import com.sally.api.project.Project;
import com.sally.api.user.User;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NamedEntityGraph(name = "UserHasProject.all",
	attributeNodes = {
		@NamedAttributeNode("project"),
		@NamedAttributeNode("user")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Table(name = "tb_user_has_project")
@Entity
public class UserHasProject {
	@EmbeddedId
	private UserHasProjectId id;

	@Enumerated(EnumType.STRING)
	private ProjectRole role;

	@Builder
	public UserHasProject(UserHasProjectId id, ProjectRole role) {
		this.id = id;
		this.role = role;
	}

	public static UserHasProject of(UserHasProjectId userHasProjectId) {
		return UserHasProject.builder()
			.id(userHasProjectId)
			.build();
	}

	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@MapsId("projectId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private Project project;

	public String userNickName() {
		return user.nickName();
	}

	public String email() {
		return user.getEmail();
	}

	public String teamName() {
		return project.getTitle();
	}
}

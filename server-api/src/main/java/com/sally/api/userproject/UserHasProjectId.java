package com.sally.api.userproject;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"userId", "projectId"})
@Embeddable
public class UserHasProjectId implements Serializable {
	private Long userId;
	private Long projectId;

	public static UserHasProjectId of(Long userId, Long projectId) {
		return new UserHasProjectId(userId, projectId);
	}
}

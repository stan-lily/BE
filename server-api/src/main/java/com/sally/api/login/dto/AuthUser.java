package com.sally.api.login.dto;

import com.sally.api.issue.domain.ProjectRole;

public interface AuthUser {
	Long userId();

	Long projectId();

	String nickName();

	String email();

	ProjectRole role();
}

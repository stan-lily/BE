package com.sally.api.login.dto;

import com.sally.api.issue.domain.ProjectRole;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class FakeAuthUser implements AuthUser {
	@Override
	public Long userId() {
		return 1L;
	}

	@Override
	public Long projectId() {
		return 1L;
	}

	@Override
	public ProjectRole role() {
		return ProjectRole.MEMBER;
	}
}

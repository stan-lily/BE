package com.sally.api.login.dto;

import com.sally.api.issue.domain.ProjectRole;

import org.springframework.stereotype.Component;

// @Profile("dev")
@Component
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
	public String nickName() {
		return "sally";
	}

	@Override
	public String email() {
		return "sally.devz@gmail.com";
	}

	@Override
	public ProjectRole role() {
		return ProjectRole.MEMBER;
	}
}

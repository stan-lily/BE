package com.sally.api.userproject.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProjectUserInfo {
	private final String nickName;
	private final String email;
	private final String teamName;

}

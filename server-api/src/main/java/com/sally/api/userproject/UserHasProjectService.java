package com.sally.api.userproject;

import com.sally.api.userproject.dto.ProjectUserInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserHasProjectService {
	private final UserHasProjectRepository userHasProjectRepository;

	@Transactional(readOnly = true)
	public List<ProjectUserInfo> getUserInfo(List<Long> userIds, Long projectId) {
		List<UserHasProject> userHasProjects = userHasProjectRepository.findAllByIdIn(
			toUserHasProjectIds(userIds, projectId));
		return userHasProjects.stream()
			.map(member -> new ProjectUserInfo(
				member.userNickName(),
				member.email(),
				member.teamName()))
			.collect(Collectors.toUnmodifiableList());
	}

	private List<UserHasProjectId> toUserHasProjectIds(List<Long> userIds, Long projectId) {
		List<UserHasProjectId> userHasProjectIds = userIds.stream()
			.map(userId -> new UserHasProjectId(userId, projectId))
			.collect(Collectors.toUnmodifiableList());
		return userHasProjectIds;
	}
}

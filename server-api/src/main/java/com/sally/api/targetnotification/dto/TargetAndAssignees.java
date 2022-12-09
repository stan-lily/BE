package com.sally.api.targetnotification.dto;

import com.sally.api.target.dto.TargetInfo;
import com.sally.api.userproject.dto.ProjectUserInfo;
import com.sally.api.util.Period;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TargetAndAssignees {
	private final TargetInfo targetInfo;
	private final List<ProjectUserInfo> assignees;

	public String title() {
		return targetInfo.getTitle();
	}

	public LocalDate targetAt() {
		return targetInfo.getTargetAt();
	}

	public Period period() {
		return targetInfo.getAssemblePeriod();
	}

	public String labelName() {
		return targetInfo.getLabelName();
	}

	public List<String> assignees() {
		return assignees.stream()
			.map(ProjectUserInfo::getNickName)
			.collect(Collectors.toUnmodifiableList());
	}

	public String assembleTitle() {
		return this.targetInfo.getAssembleTitle();
	}
}

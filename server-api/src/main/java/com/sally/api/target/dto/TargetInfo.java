package com.sally.api.target.dto;

import com.sally.api.util.Period;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TargetInfo {
	private final String title;
	private final LocalDate targetAt;
	private final Period assemblePeriod;
	private final String assembleTitle;
	private final String projectTitle;
	private final String labelName;

	public LocalDate startAt() {
		return assemblePeriod.getStartDate();
	}

	public LocalDate endAt() {
		return assemblePeriod.getEndDate();
	}
}

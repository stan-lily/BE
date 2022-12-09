package com.sally.api.targetnotification.dto;

import com.sally.api.util.Period;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TargetEmailRequest {
	private String email;
	private String targetTitle;
	private LocalDate targetAt;
	private String assembleTitle;
	private LocalDate assembleStartAt;
	private LocalDate assembleEndAt;
	private String labelName;

	public Period toPeriod() {
		return Period.of(assembleStartAt, assembleEndAt);
	}
}

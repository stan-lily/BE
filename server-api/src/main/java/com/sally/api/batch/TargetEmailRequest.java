package com.sally.api.batch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sally.api.util.Period;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TargetEmailRequest {
	private String email;
	private String targetTitle;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate targetAt;
	private String assembleTitle;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate assembleStartAt;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate assembleEndAt;
	private String labelName;

	public Period toPeriod() {
		return Period.of(assembleStartAt, assembleEndAt);
	}
}

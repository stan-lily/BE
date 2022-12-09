package com.sally.batch.job;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate targetAt;
	private String assembleTitle;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate assembleStartAt;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate assembleEndAt;
	private String labelName;
}

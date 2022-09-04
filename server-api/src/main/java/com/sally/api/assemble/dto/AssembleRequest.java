package com.sally.api.assemble.dto;

import com.sally.api.assemble.Assemble;
import com.sally.api.project.Project;
import com.sally.api.project.dto.ProjectInfo;
import com.sally.api.util.Period;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AssembleRequest {
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CreationDto {
		@NotBlank(message = "어셈블 제목을 입력 하세요.")
		@Size(max = 100, message = "어셈블 제목은 100자 이내 입니다.")
		private String assembleTitle;
		// @FutureOrPresent(message = "오늘 이후 부터 등록 가능합니다.")
		private LocalDate assembleStartAt;
		// @Future(message = "오늘 이후 부터 등록 가능합니다.")
		private LocalDate assembleEndAt;

		public Assemble toEntity(ProjectInfo projectInfo) {
			return Assemble.createFrom(
				assembleTitle,
				assembleStartAt,
				assembleEndAt,
				Project.from(projectInfo.getId()));
		}

		public Period toPeriod() {
			return Period.of(assembleStartAt, assembleEndAt);
		}
	}
}

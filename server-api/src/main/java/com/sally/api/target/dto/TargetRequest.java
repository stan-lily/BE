package com.sally.api.target.dto;

import com.sally.api.assemble.Assemble;
import com.sally.api.label.Label;
import com.sally.api.target.Target;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TargetRequest {
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreationDto {
		@NotNull
		private Long labelId;
		@NotBlank
		@Size(min = 1, max = 80)
		private String title;
		// @FutureOrPresent  // TODO 배포 후 테스트 위해 임시로 주석처리 했습니다.
		private LocalDate targetAt;

		public Target toEntity(Long assembleId) {
			return Target.createOf(title, targetAt, Assemble.from(assembleId), Label.from(labelId));
		}
	}
}

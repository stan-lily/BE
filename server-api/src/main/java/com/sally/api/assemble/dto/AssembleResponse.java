package com.sally.api.assemble.dto;

import com.sally.api.assemble.Assemble;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AssembleResponse {
	@Getter
	@AllArgsConstructor
	public static class Lists {
		private final List<AssembleResponse.AssembleDto> assembleDtos;
	}

	@Getter
	public static class AssembleDto {
		private final Long id;
		private final String title;
		private final LocalDate startAt;
		private final LocalDate endAt;

		public AssembleDto(Assemble assemble) {
			this.id = assemble.id();
			this.title = assemble.title();
			this.startAt = assemble.startAt();
			this.endAt = assemble.endAt();
		}
	}
}

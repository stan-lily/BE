package com.sally.api.milestone.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class MilestoneResponse {
	@Getter
	@NoArgsConstructor
	public static class Lists {
		private List<MilestoneDto> milestones;

		public Lists(List<MilestoneDto> milestones) {
			this.milestones = milestones;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class MilestoneDto {
		private Long id;
		private String title;
		private String description;
		@JsonFormat(pattern = "yyyy-MM-dd")
		private LocalDate startDate;
		@JsonFormat(pattern = "yyyy-MM-dd")
		private LocalDate completionDate;
		private Long openStatusCount;
		private Long closeStatusCount;

		public MilestoneDto(
			Long id,
			String title,
			String description,
			LocalDate startDate,
			LocalDate completionDate,
			Long openStatusCount,
			Long closeStatusCount) {
			this.id = id;
			this.title = title;
			this.description = description;
			this.startDate = startDate;
			this.completionDate = completionDate;
			this.openStatusCount = openStatusCount;
			this.closeStatusCount = closeStatusCount;
		}
	}
}

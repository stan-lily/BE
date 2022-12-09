package com.sally.api.target.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sally.api.target.TargetCountByDate;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class TargetResponse {
	@Getter
	@AllArgsConstructor
	public static class TargetCountsDto {
		private List<TargetCountsByDays> targetCountsByDays;

		public static TargetCountsDto from(List<TargetCountsByDays> targetCountsByDays) {
			return new TargetCountsDto(targetCountsByDays);
		}
	}

	@Getter
	public static class TargetCountsByDays {
		private int count;
		@JsonFormat(pattern = "yyyy-MM-dd")
		private LocalDate date;

		public TargetCountsByDays(TargetCountByDate targetInfo) {
			this.count = targetInfo.getTargetCount();
			this.date = targetInfo.getEndAt();
		}
	}

	@Getter
	@AllArgsConstructor
	public static class TargetDto {
		private List<TargetByDays> targets;

		public static TargetDto from(List<TargetByDays> targetByDays) {
			return new TargetDto(targetByDays);
		}
	}

	@Getter
	@AllArgsConstructor
	public static class TargetByDays {
		private Long targetId;
		@JsonFormat(pattern = "yyyy-MM-dd")
		private LocalDate targetAt;
		private String targetTitle;
		private Long labelId;
		private String labelBackgroundColor;

		public static TargetByDays of(Long targetId, LocalDate targetAt, String title, Long labelId,
			String labelBackgroundColor) {
			return new TargetByDays(targetId, targetAt, title, labelId, labelBackgroundColor);
		}
	}
}

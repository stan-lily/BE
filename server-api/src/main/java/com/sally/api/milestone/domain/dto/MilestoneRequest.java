package com.sally.api.milestone.domain.dto;

import com.sally.api.milestone.domain.Milestone;
import com.sally.api.project.Project;
import com.sally.api.project.dto.ProjectInfo;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MilestoneRequest {
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreationDto {
		@NotBlank
		private String title;
		private LocalDate startDate;
		private LocalDate completionDate;
		@NotBlank
		@Size(max = 50)
		private String description;

		public Milestone toEntity(ProjectInfo projectInfo) {
			return Milestone.of(title, description, startDate, completionDate, Project.from(projectInfo.getId()));
		}
	}
}

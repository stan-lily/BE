package com.sally.api.label.dto;

import com.sally.api.label.Label;
import com.sally.api.project.dto.ProjectInfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LabelRequest {
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LabelDto {
		@NotBlank(message = "라벨명을 입력하세요")
		@Size(max = 20, message = "라벨명을 입력하세요")
		private String name;
		private String description;
		@Size(min = 7, max = 7)
		private String backgroundColor;
		@NotBlank
		private String fontColor;

		public Label toEntity(ProjectInfo projectInfo) {
			return Label.createFrom(name, description, backgroundColor, fontColor, projectInfo.getId());
		}
	}
}

package com.sally.api.label.dto;

import com.sally.api.label.Label;
import com.sally.api.project.Project;

public class LabelRequest {
	public static class Creation {
		private String name;
		private String description;
		private String backgroundColor;
		private String fontColor;

		public Label toEntity(Project project) {
			return Label.builder()
				.name(name)
				.description(description)
				.backgroundColor(backgroundColor)
				.fontColor(fontColor)
				.project(project)
				.build();
		}
	}
}

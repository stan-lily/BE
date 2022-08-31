package com.sally.api.label.dto;

import com.sally.api.label.Label;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class LabelResponse {
	@Getter
	@AllArgsConstructor
	public static class Lists {
		private final List<LabelDto> labels;
	}

	@Getter
	public static class LabelDto {
		private final Long id;
		private final String name;
		private final String description;
		private final String backgroundColor;
		private final String fontColor;

		public LabelDto(Label label) {
			this.id = label.id();
			this.name = label.name();
			this.description = label.description();
			this.backgroundColor = label.backgroundColor();
			this.fontColor = label.fontColorByText();
		}
	}
}

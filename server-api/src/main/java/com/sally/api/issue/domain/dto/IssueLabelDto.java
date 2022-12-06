package com.sally.api.issue.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IssueLabelDto {
	private String name;
	private String backgroundColor;
	private String fontColor;

	public IssueLabelDto(String name, String backgroundColor, String fontColor) {
		this.name = name;
		this.backgroundColor = backgroundColor;
		this.fontColor = fontColor;
	}
}

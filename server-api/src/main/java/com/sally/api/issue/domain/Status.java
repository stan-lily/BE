package com.sally.api.issue.domain;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum Status {
	OPEN("open"), CLOSE("closed");

	private final String text;

	Status(String text) {
		this.text = text;
	}

	public static Status of(String statusStr) {
		return Arrays.stream(values())
			.parallel()
			.filter(status -> status.text.equals(statusStr))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("no Issue status"));
	}
}

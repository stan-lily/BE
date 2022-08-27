package com.sally.api.label;

import java.util.Arrays;

public enum TextColor {
	BRIGHT, DARK;

	public static TextColor from(String fontColor) {
		return Arrays.stream(values()).parallel().findAny().orElseThrow();
	}
}

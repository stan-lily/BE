package com.sally.api.targetnotification.dto;

import com.sally.api.util.Period;
import com.sally.api.util.PeriodFormatter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Builder;

public class TargetEmailDto {
	private final String toEmail;
	private final String assembleTitle;
	private final String targetTitle;
	private final Period period;
	private final LocalDate targetAt;
	private final String labelName;
	private final List<String> targetAssignees;

	@Builder
	public TargetEmailDto(String toEmail, String assembleTitle, String targetTitle, Period period,
		LocalDate targetAt, String labelName, List<String> targetAssignees) {
		this.toEmail = toEmail;
		this.assembleTitle = assembleTitle;
		this.targetTitle = targetTitle;
		this.period = period;
		this.targetAt = targetAt;
		this.labelName = labelName;
		this.targetAssignees = targetAssignees;
	}

	public String subject() {
		String assembleTitleText = String.format("[ %s 데모 ] - ", assembleTitle);
		String periodText = String.format("( %s )", PeriodFormatter.toDate(period));
		return new StringBuffer()
			.append(assembleTitleText)
			.append(targetTitle)
			.append(periodText)
			.toString();
	}

	public Map<String, Object> getProperties() {
		final Map<String, Object> properties = new ConcurrentHashMap<>();
		properties.put("title", targetTitle);
		properties.put("period", PeriodFormatter.toDate(period));
		properties.put("targetAt", targetAt);
		if (!Objects.isNull(targetAssignees)) {
			properties.put("assignees", targetAssignees);
		}
		properties.put("label", labelName);
		return properties;
	}

	public String getEmail() {
		return toEmail;
	}
}

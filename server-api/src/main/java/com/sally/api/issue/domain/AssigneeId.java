package com.sally.api.issue.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"userId", "projectId", "issueId"})
@Embeddable
public class AssigneeId implements Serializable {
	private Long userId;
	private Long projectId;
	private Long issueId;
}

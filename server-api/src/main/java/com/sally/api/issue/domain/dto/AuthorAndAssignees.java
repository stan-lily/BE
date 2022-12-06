package com.sally.api.issue.domain.dto;

import java.util.List;
import java.util.Objects;

/**
 * 전달시 생성자에 의해 구분지어 값을 받습니다.
 * - Issue 등록시 필수 값인 author는 final로 생성자를 통해 제한합니다.
 * - allIds() : UserRepository 조회시 id 전달 경우 비필수 값에 대한 null 체크 처리후 반환 합니다.
 */
public class AuthorAndAssignees {
	private final Long author;
	private List<Long> assignees;

	public AuthorAndAssignees(Long author, List<Long> assignees) {
		this.author = author;
		this.assignees = assignees;
	}

	public AuthorAndAssignees(Long author) {
		this.author = author;
	}

	public List<Long> assignees() {
		return this.assignees;
	}

	public Long author() {
		return author;
	}

	public List<Long> allIds() {
		if (hasAssignees()) {
			this.assignees().add(author);
			return this.assignees();
		}
		return List.of(author);
	}

	private boolean hasAssignees() {
		return !Objects.isNull(assignees) && assignees.size() > 0;
	}

	// 필수값인 author 만 있으면 된다.
	public boolean isNull() {
		return !hasAuthor();
	}

	private boolean hasAuthor() {
		return !Objects.isNull(author) || author > 0L;
	}
}

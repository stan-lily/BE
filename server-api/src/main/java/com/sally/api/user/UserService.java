package com.sally.api.user;

import com.sally.api.issue.domain.dto.AuthorAndAssignees;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public void isValidAndGet(AuthorAndAssignees authorAndAssignees) {
		// 해당 id 들의 존재 여부를 검증한다.
		if (authorAndAssignees.isNull()) {
			throw new IllegalArgumentException("invalid null of parameters");
		}
		boolean userIsExisted = userRepository.existsByIdIn(authorAndAssignees.allIds());
		if (!userIsExisted) {
			throw new IllegalArgumentException("no exist user");
		}
	}

	@Transactional(readOnly = true)
	public User getByUserIdOrThrow(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("user not found"));
	}
}

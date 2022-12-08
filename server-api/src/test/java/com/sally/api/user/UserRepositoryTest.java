package com.sally.api.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("사용자 리포지토리는 사용자 조회 결과 없으면 false를 반환한다.")
	void userService_noMatchedId_isValidAndGet() {
		boolean actual = userRepository.existsByIdIn(List.of(Long.MAX_VALUE, Long.MAX_VALUE - 1L));
		assertThat(actual).isFalse();
	}
}
